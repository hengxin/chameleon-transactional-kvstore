package twopc.coordinator;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import client.context.AbstractClientContext;
import rmi.IRemoteSite;
import twopc.participant.I2PCParticipant;

/**
 * Coordinator of 2PC protocol for RVSI transactions.
 * <p><del> FIXME The coordinator executes an optimized 2PC protocol
 * with "early commit notification".</del>
 * @implNote
 * 	This coordinator implementation is based on class {@link Phaser} introduced since Java 7.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public final class RVSI2PCPhaserCoordinator extends Abstract2PCCoordinator {

	private static final Logger LOGGER = LoggerFactory.getLogger(RVSI2PCPhaserCoordinator.class);
	
	private static final ExecutorService exec = Executors.newCachedThreadPool();
	
	protected enum Phase { PREPARE, COMMIT, ABORT }
	protected final Phaser phaser = new CommitPhaser();	// FIXME Is it a good idea to create an object of a nested class here?

	private final VersionConstraintManager vcm;

	/**
	 * @param ctx	client context 
	 * @param vcm	RVSI-specific version constraint manager   
	 */
	public RVSI2PCPhaserCoordinator(final AbstractClientContext ctx, 
								   final VersionConstraintManager vcm)  {
		super(ctx);
		this.vcm = vcm;
	}

	@Override
	public boolean execute2PC(final ToCommitTransaction tx) {
		final Map<IRemoteSite, ToCommitTransaction> site_tx_map = super.ctx.partition(tx);
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
	 * {@link CommitPhaser} controls the two phases of the 2PC protocol:
	 * the "prepare" phase and the "commit/abort" phase.
	 * 
	 * @author hengxin
	 * @date Created on Dec 28, 2015
	 * 
	 * @implNote
	 * {@link CommitPhaser} extends {@link Phaser} introduced since Java 7.
	 */
	private final class CommitPhaser extends Phaser {

		private final Logger LOGGER = LoggerFactory.getLogger(CommitPhaser.class);

		/**
		 * Coordinate the two phases on behalf of the enclosing {@link RVSI2PCPhaserCoordinator}, including
		 * <ul>
		 * <li> Collecting results from each phase and computing for the next one
		 * <li> Logging for each phase
		 * <ul>
		 */
		@Override
		protected boolean onAdvance(int phase, int registeredParties) {
			Abstract2PCCoordinator coord = RVSI2PCPhaserCoordinator.super;

			switch (phase) {
			case 0:
				LOGGER.info("All [{}] masters have been finished the [{}] phase.", registeredParties, Phase.PREPARE);

				/**
				 * check the decisions of all participants during the Phase#PREPARE phase,
				 * and determine whether to commit or abort the transaction:
				 * if all #prepared_decesions are true, then commit; otherwise, abort.
				 */
				coord.to_commit_decision = coord.prepared_decisions.values().stream().allMatch(decision -> decision);

				LOGGER.info("The commit/abort decision for the [{}] phase is [{}].", Phase.COMMIT, coord.to_commit_decision);
				return false;	// this phaser has not yet finished

			case 1:
				LOGGER.info("All [{}] masters have been finished the [{}] phase.", registeredParties, Phase.COMMIT); 

				/**
				 * check the decisions of all participants during the Phase#COMMIT phase,
				 * and compute the final committed/abort state of the transaction:
				 * the transaction is committed if and only if
				 * (1) #to_committed_decision is true </i>and</i> 
				 * (2) #comitted_decisions of all participants are true.
				 */
				coord.is_committed = coord.to_commit_decision 
						&& coord.committed_decisions.values().stream().allMatch(decision -> decision);

				return true;	// this phaser has finished its job.

			default:
				return true;	
			}
		}
	}
}
