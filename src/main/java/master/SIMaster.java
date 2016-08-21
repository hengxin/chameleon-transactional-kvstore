package master;

import com.sun.istack.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import context.AbstractContext;
import exception.transaction.TransactionCommunicationException;
import exception.transaction.TransactionExecutionException;
import jms.master.JMSPublisher;
import kvs.component.Timestamp;
import kvs.compound.CKeyToOrdinalIndex;
import kvs.table.MasterTable;
import master.mvcc.StartCommitLogs;
import site.ITransactional;
import twopc.participant.I2PCParticipant;

import static twopc.coordinator.phaser.CommitPhaser.Phase.ABORT;
import static twopc.coordinator.phaser.CommitPhaser.Phase.COMMIT;
import static twopc.coordinator.phaser.CommitPhaser.Phase.PREPARE;

/**
 * Master employs an MVCC protocol to locally implement (<i>nearly but not really</i>) 
 * snapshot isolation (SI, for short).
 * @description
 * 	<i>Nearly but not really SI:</i> because Chameleon tends to tolerate weak 
 * 	transactional consistency, it does not require the read operations of a 
 *  transaction with start-timestamp <i>sts</i> to obtain the <i>latest</i> preceding 
 *  version committed before <i>sts</i>. 
 *  <p>
 *  This design choice has an effect on the implementation of both 
 *  {@link #get(kvs.component.Row, kvs.component.Column)} and 
 *  {@link #commit(ToCommitTransaction, VersionConstraintManager)}, especially on
 *  their synchronization and locking strategies.
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 */
public final class SIMaster extends AbstractMaster implements ITransactional, I2PCParticipant {

	private static final Logger LOGGER = LoggerFactory.getLogger(SIMaster.class);
	private final ExecutorService exec = Executors.newCachedThreadPool();
	
	private AtomicLong ts = new AtomicLong(0);	// for generating start-timestamps and commit-timestamps; will be accessed concurrently
	private final StartCommitLogs logs = new StartCommitLogs();	// commit log: each entry is composed of start-timestamp, commit-timestamp, and buffered updates of a transaction
	private final CKeyToOrdinalIndex ck_ord_index = new CKeyToOrdinalIndex();

	/**
	 * Constructor with {@link MasterTable} as the default underlying table
	 * and with {@link JMSPublisher} as the default underlying 
	 * mechanism of message propagation.
	 * @param context	context for the master site
	 */
	public SIMaster(AbstractContext context) { this(context, new JMSPublisher()); }
	
	/**
	 * Constructor with {@link MasterTable} as the default underlying table
	 * and with user-specified {@link JMSPublisher} as the underlying
	 * mechanism of message propagation.
	 * @param context	context for the master site
	 * @param jms_publisher		the underlying mechanism of message propagation; 
	 * 	it can be {@code null} if this master site does not need to propagate messages. 
	 * @implNote
	 *   FIXME removing the default {@link MasterTable}; putting it into the parameters.
	 */
	public SIMaster(AbstractContext context, @Nullable JMSPublisher jms_publisher) {
		super(context, jms_publisher);
		super.table = new MasterTable();	// the underlying database in the "table" form
	}
	
	/**
	 * Start a transaction: generate and assign a new start-timestamp.
	 * @return A start-timestamp 
	 * @throws TransactionExecutionException 
	 */
	@Override
	public Timestamp start() throws TransactionExecutionException {
	    LOGGER.debug("[{}] starts a transaction.", this.getClass().getSimpleName());

        // Using implicit {@link Future} to get the result; also use Java 8 Lambda expression
		try {
			return new Timestamp(exec.submit( () -> ts.incrementAndGet()).get());
		} catch (InterruptedException ie) {
			String msg = "Failed to start a transaction due to unexpected thread interruption.";
			LOGGER.warn(msg, ie.getCause());	// log at the master site side
			throw new TransactionExecutionException(msg, ie);	// thrown to the client side
		} catch (ExecutionException ee) {
			String msg = "Failed to generate a new start-timestamp for a transaction to start.";
			LOGGER.warn(msg, ee.getCause());	// log at the master site side
			throw new TransactionExecutionException(msg, ee);	// thrown to the client side
		}
	}

	/**
	 * Try to commit a transaction:
	 * <p><ol> 
	 * <li> check version constraints; check write-conflict-free
	 * <li> generate and assign commit-timestamp
	 * <li> update start-commit-log
	 * <li> apply buffered-updates to the in-memory table
	 * <li> propagate the updates
	 * </ol><p>
	 * <b>Note:</b> (3) and (4) cannot be re-ordered!
	 * @throws TransactionExecutionException 
	 * 	Thrown if an error occurs during commit (i.e., the transaction is not normally aborted
	 *  according to the commit protocol).
	 *  
	 * @implNote
	 * 	Key points about synchronization issues and locking strategies: 
	 *  <p>
	 *  <ul>
	 *  <li> The wcf check, cts generation, and commit-log update should be protected by
	 *  	the same write-lock of {@link #logs}.
	 *  <li> No need to synchronize {@link #get(kvs.component.Row, kvs.component.Column)}
	 *    	with the update of the underlying table under the read/write-lock of {@link #logs}.
	 *  </ul>
	 */
	@Deprecated
	@Override
	public boolean commit(ToCommitTransaction tx, VersionConstraintManager vcm)
            throws TransactionExecutionException {
		Future<Boolean> future_committed = exec.submit(() -> {
			Timestamp cts = null;

			/**
			 * {@link VersionConstraintManager} is local to this method.
			 * Thus vc (version-constraint) can be checked separately from wcf (write-conflict free). 
			 */
			boolean vc_checked = vcm.check();
			boolean wcf_checked = false;
			boolean can_committed = false;

			logs.writeLock.lock();
			try {
				wcf_checked = logs.wcf(tx);
				can_committed = vc_checked && wcf_checked;

				if (can_committed) {	// (1) check 
					cts = new Timestamp(ts.incrementAndGet());	// (2) commit-timestamp; commit in "commit order"
					logs.addStartCommitLog(tx.getSts(), cts, tx.getBufferedUpdates().fillTsAndOrd(cts, ck_ord_index));	// (3) update start-commit-log
				}
			} finally {
				logs.writeLock.unlock();
			}

			if(can_committed) {
				/**
				 * Chameleon does not require read operations of a transaction to get the <i>latest</i> version 
				 * before the sts of that transaction. Thus it is not necessary for the master to synchronize the 
				 * updates to the underlying table with read operations, both guarded by the lock on #logs. 
				 */
				table.apply(cts, tx.getBufferedUpdates());	// (4) apply buffered-updates to the in-memory table
				try {
					super.messenger.ifPresent(messenger -> messenger.send(tx));	// (5) propagate 
				} catch (TransactionCommunicationException tae) {
					LOGGER.warn(tae.getMessage(), tae.getCause());
				}		
			}

			return can_committed;
		});
		
		try {
			return future_committed.get();
		} catch (InterruptedException | ExecutionException ieee) {
			String msg = String.format("Transaction aborted due to unexpected causes: %s.", ieee.getMessage());
			LOGGER.error(msg, ieee.getCause());	// log at the master side
			throw new TransactionExecutionException(msg, ieee.getCause());		// thrown to the client side
		}
	}

    @Override
    public boolean prepare(ToCommitTransaction tx, VersionConstraintManager vcm)
            throws RemoteException, TransactionExecutionException {
        LOGGER.info("[{}] begins the [{}] phase with tx [{}] and vcm [{}]",
                this.getClass().getSimpleName(), PREPARE, tx, vcm);

        Future<Boolean> prepareFuture = exec.submit(() -> {
            /**
             * {@link VersionConstraintManager} is local to this method.
             * Thus vc (version-constraint) can be checked separately from wcf (write-conflict free).
             */
            logs.writeLock.lock();

            LOGGER.debug("The result of checking vcm [{}] is [{}].", vcm, vcm.check());
            LOGGER.debug("The result of checking wcf against tx [{}] and logs [{}] is [{}].",
                    tx, logs, logs.wcf(tx));

            return vcm.check() && logs.wcf(tx);
        });

        try {
            return prepareFuture.get();
        } catch (InterruptedException | ExecutionException ieee) {
            String msg = String.format("Transaction aborted due to unexpected causes: %s.", ieee.getMessage());
            LOGGER.error(msg, ieee.getCause());	// log at the master side
            throw new TransactionExecutionException(msg, ieee.getCause());		// thrown to the client side
        }
    }

    @Override
    public boolean commit(ToCommitTransaction tx, Timestamp cts)
            throws RemoteException, TransactionExecutionException {
        LOGGER.info("[{}] begins the [{}] phase with tx [{}] and cts [{}]",
                this.getClass().getSimpleName(), COMMIT, tx, cts);

        // update start-commit-log
        logs.addStartCommitLog(tx.getSts(), cts,
                tx.getBufferedUpdates().fillTsAndOrd(cts, ck_ord_index));
        logs.writeLock.unlock();

        /**
         * Chameleon does not require read operations of a transaction to
         * get the <i>latest</i> version before the sts of that transaction.
         * Thus it is not necessary for the master to synchronize the updates
         * to the underlying table with read operations, both guarded by the lock on #logs.
         */
        table.apply(cts, tx.getBufferedUpdates());	// apply buffered-updates to the in-memory table
        try {
            super.messenger.ifPresent(messenger -> messenger.send(tx));	// propagate
        } catch (TransactionCommunicationException tae) {
            LOGGER.warn(tae.getMessage(), tae.getCause());
        }

        return true;
    }

    @Override
    public void abort() {
        LOGGER.info("[{}] begins the [{}] phase.", this.getClass().getSimpleName(), ABORT);

        // TODO what else to do?
        logs.writeLock.unlock();
    }

}