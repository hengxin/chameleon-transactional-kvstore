package twopc.coordinator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import client.context.AbstractClientContext;
import exception.transaction.TransactionExecutionException;
import kvs.component.Timestamp;
import twopc.coordinator.phaser.CommitPhaser;
import twopc.participant.I2PCParticipant;
import twopc.timing.CentralizedTimestampOracle;

import static java.util.stream.Collectors.toList;

/**
 * Coordinator of 2PC protocol for RVSI transactions.
 * <p><del> FIXME The coordinator executes an optimized 2PC protocol
 * with "early commit notification".</del>
 * @implNote
 * 	This coordinator implementation is based on class {@link Phaser} introduced since Java 7.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public class RVSI2PCPhaserCoordinator extends Abstract2PCCoordinator {
	private transient static final Logger LOGGER = LoggerFactory.getLogger(RVSI2PCPhaserCoordinator.class);
	private transient static final ExecutorService exec = Executors.newCachedThreadPool();

	transient Phaser phaser;  // TODO put it in {@link Abstract2PCCoordinator}

    /**
	 * @param ctx	client context 
	 */
	public RVSI2PCPhaserCoordinator(final AbstractClientContext ctx)  {
		super(ctx);
        phaser = new CommitPhaser(this);   // TODO Is it safe to pass {@code this} reference?
	}

	@Override
	public boolean execute2PC(final ToCommitTransaction tx, final VersionConstraintManager vcm)
            throws RemoteException, TransactionExecutionException {
		final Map<Integer, ToCommitTransaction> siteTxMap = cctx.partition(tx);
        final Map<Integer, VersionConstraintManager> siteVcmMap = cctx.partition(vcm);

		List<Callable<Boolean>> tasks = siteTxMap.keySet().stream()
				.map(index -> new CommitPhaserTask(this, (I2PCParticipant) cctx.getMaster(index),
                        siteTxMap.get(index), siteVcmMap.get(index)))
                .collect(toList());
		
		try {
			exec.invokeAll(tasks);	// blocking here
		} catch (InterruptedException ie) {
		    String msg = "2PC protocol has been interrupted unexpectedly.";
			LOGGER.error(msg, ie);
            throw new TransactionExecutionException(msg, ie);
		}

		// FIXME the return value
		return is_committed;
	}


    /**
     * Check the decisions of all participants during the Phase#PREPARE phase,
     * and determine whether to commit or abort the transaction:
     * if all #prepared_decesions are true, then commit; otherwise, abort.
     */
    @Override
    public boolean onPreparePhaseFinished() {
        to_commit_decision = prepared_decisions.values().stream().allMatch(decision -> decision);
        // TODO check k3SI condition
        if (to_commit_decision)
            cts = new Timestamp(CentralizedTimestampOracle.INSTANCE.get());
        return to_commit_decision;
    }

    /**
     * Check the decisions of all participants during the Phase#COMMIT phase,
     * and compute the final committed/abort state of the transaction:
     * the transaction is committed if and only if
     * (1) #to_committed_decision is true </i>and</i>
     * (2) #comitted_decisions of all participants are true.
     */
    @Override
    public boolean onCommitPhaseFinished() {
        is_committed = to_commit_decision
                && committed_decisions.values().stream().allMatch(decision -> decision);
        return is_committed;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        phaser = new CommitPhaser(this);
    }
}