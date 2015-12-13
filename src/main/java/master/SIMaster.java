package master;

import java.rmi.ConnectIOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import exception.transaction.TransactionCommunicationException;
import exception.transaction.TransactionExecutionException;
import jms.master.JMSCommitLogPublisher;
import kvs.component.Timestamp;
import kvs.compound.CKeyToOrdinalIndex;
import kvs.table.MasterTable;
import master.context.MasterContext;
import master.mvcc.StartCommitLogs;
import messages.AbstractMessage;
import messages.IMessageProducer;
import site.AbstractSite;

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
 *  {@link #read(kvs.component.Row, kvs.component.Column)} and 
 *  {@link #commit(ToCommitTransaction, VersionConstraintManager)}, especially on
 *  their synchronization and locking strategies.
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 */
public class SIMaster extends AbstractSite implements IMessageProducer
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SIMaster.class);

	private final ExecutorService exec = Executors.newCachedThreadPool();
	
	private AtomicLong ts = new AtomicLong(0);	// for generating start-timestamps and commit-timestamps; will be accessed concurrently
	private final StartCommitLogs logs = new StartCommitLogs();	// commit log: each entry is composed of start-timestamp, commit-timestamp, and buffered updates of a transaction
	private final CKeyToOrdinalIndex ck_ord_index = new CKeyToOrdinalIndex();

	/**
	 * Constructor with {@link MasterContext}.
	 */
	public SIMaster(MasterContext context)
	{
		super.context = context;
		super.table = new MasterTable();	// the underlying database in the "table" form
	}
	
	/**
	 * Start a transaction: generate and assign a new start-timestamp.
	 * 
	 * @return 
	 * 		A start-timestamp 
	 * @throws ConnectIOException
	 * @throws TransactionExecutionException 
	 */
	@Override
	public Timestamp start() throws TransactionExecutionException
	{
        // Using implicit {@link Future} to get the result; also use Java 8 Lambda expression
		try
		{
			return new Timestamp(exec.submit( () -> 
			{
				return this.ts.incrementAndGet();
			}).get());
		} catch (InterruptedException ie)
		{
			String msg = "Failed to start a transaction due to unexpected thread interruption.";
			LOGGER.warn(msg, ie.getCause());	// log at the master site side
			throw new TransactionExecutionException(msg, ie);	// thrown to the client side
		} catch (ExecutionException ee)
		{
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
	 *  <li> No need to synchronize {@link #read(kvs.component.Row, kvs.component.Column)}
	 *    	with the update of the underlying table under the read/write-lock of {@link #logs}.
	 *  </ul>
	 * 
	 * <p> TODO Start a new thread? 
	 */
	@Override
	public boolean commit(ToCommitTransaction tx, VersionConstraintManager vc_manager) throws TransactionExecutionException
	{
		Timestamp cts = null;
		
		/**
		 * {@link VersionConstraintManager} is local to this method.
		 * Thus vc (version-constraint) can be checked separately from wcf (write-conflict free). 
		 */
		boolean vc_checked = vc_manager.check();
		boolean wcf_checked = false;
		boolean can_committed = false;
		
		try
		{
			if(this.logs.write_lock.tryLock(1, TimeUnit.SECONDS))
			{
				wcf_checked = this.logs.wcf(tx);
				can_committed = vc_checked && wcf_checked;
				
				if (can_committed)	// (1) check
				{
					cts = new Timestamp(this.ts.incrementAndGet());	// (2) commit-timestamp
					this.logs.addStartCommitLog(tx.getSts(), cts, tx.getBufferedUpdates().fillTsAndOrd(cts, this.ck_ord_index));	// (3) update start-commit-log
				}
			}
		} catch (InterruptedException ie)
		{
			String log = String.format("Failed to commit transaction (%s) due to unexpected thread interruption.", tx);
			LOGGER.error(log, ie.getCause());	// log at the master site side
			throw new TransactionExecutionException(String.format(log), ie.getCause());	// thrown to the client side
		} finally
		{
			this.logs.write_lock.unlock();
		}
		
		if(can_committed)
		{
			/**
			 * Chameleon does not require read operations of a transaction to get the <i>latest</i> version 
			 * before the sts of that transaction. Thus it is not necessary for the master 
			 * to synchronize the updates to the underlying table with read operations, both guarded by the lock on
			 * #logs. 
			 */
			this.table.apply(cts, tx.getBufferedUpdates());	// (4) apply buffered-updates to the in-memory table
			try
			{
				this.send(tx); // (5) propagate
			} catch (TransactionCommunicationException tae)
			{
				LOGGER.warn(tae.getMessage(), tae.getCause());
			}		
		}
		
		return can_committed;
	}

	@Override
	public void send(AbstractMessage msg) throws TransactionCommunicationException
	{
		if(super.jmser.isPresent())
		{
			try
			{
				((JMSCommitLogPublisher) super.jmser.get()).publish(msg);
				LOGGER.info("The master [{}] has published the commit log [{}] to its slaves.", this, msg);
			} catch (JMSException jmse)
			{
				throw new TransactionCommunicationException(String.format("I [{}] Failded to publish the commit log [{}]. \n {}", super.context.self(), msg), jmse.getCause());
			}
		}
		else 
		{
			LOGGER.warn("The master [{}] has not been registered as a JMS publisher. Please call registerAsJMSParticipant() first to make it able to publish messages.", this);
		}
	}
}
