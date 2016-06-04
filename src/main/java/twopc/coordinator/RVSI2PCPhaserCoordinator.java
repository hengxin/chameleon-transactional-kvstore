package twopc.coordinator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import client.context.AbstractClientContext;
import site.ISite;
import twopc.coordinator.phaser.CommitPhaser;
import twopc.participant.I2PCParticipant;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(RVSI2PCPhaserCoordinator.class);
	private final ExecutorService exec = Executors.newCachedThreadPool();
	
	Phaser phaser;  // TODO put it in {@link Abstract2PCCoordinator}

	private final VersionConstraintManager vcm;

	/**
	 * @param ctx	client context 
	 * @param vcm	RVSI-specific version constraint manager   
	 */
	public RVSI2PCPhaserCoordinator(final AbstractClientContext ctx,
								   final VersionConstraintManager vcm)  {
		super(ctx);
		this.vcm = vcm;
        this.phaser = new CommitPhaser(this);   /** TODO Is it safe to pass {@code this} reference? */
	}

	@Override
	public boolean execute2PC(final ToCommitTransaction tx) {
		final Map<ISite, ToCommitTransaction> site_tx_map = super.ctx.partition(tx);
		// TODO split the vcm

		List<Callable<Boolean>> task_list = site_tx_map.entrySet().stream()
				.map(site_tx_entry -> new CommitPhaserTask(
						this, (I2PCParticipant) site_tx_entry.getKey(), site_tx_entry.getValue(), this.vcm))	// the fourth @param vcm
				.collect(toList());
		
		try {
			exec.invokeAll(task_list);	// blocking here
		} catch (InterruptedException ie) {
			LOGGER.error("2PC protocol has been interrupted unexpectedly.", ie);	// FIXME fault-handling???
		}

		// FIXME the return value
		return false;
	}


    /**
     * Check the decisions of all participants during the Phase#PREPARE phase,
     * and determine whether to commit or abort the transaction:
     * if all #prepared_decesions are true, then commit; otherwise, abort.
     */
    @Override
    public boolean onPreparePhaseFinished() {
        to_commit_decision = prepared_decisions.values().stream().allMatch(decision -> decision);
        return to_commit_decision;
    }

    /**
     * check the decisions of all participants during the Phase#COMMIT phase,
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

}
