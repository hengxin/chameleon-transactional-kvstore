package twopc.coordinator;

import java.util.concurrent.Phaser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link CommitPhaser} controls the two phases of the 2PC protocol:
 * the "prepare" phase and the "commit/abort" phase.
 * 
 * @author hengxin
 * @date Created on Dec 28, 2015
 * 
 * @implNote
 * {@link CommitPhase} extends {@link Phaser} introduced since Java 7.
 */
public final class CommitPhaser extends Phaser {

	private final static Logger LOGGER = LoggerFactory.getLogger(CommitPhaser.class);
	
	public enum Phase { PREPARE, COMMIT, ABORT }
	
	private final ICoordinator coordinator;
	
	/**
	 * Constructor of {@link CommitPhaser} with its coordinator (i.e., executor) {@link ICoordinator}. 
	 * @param coordinator	{@link ICoordinator} of this {@link CommitPhaser}
	 */
	public CommitPhaser(ICoordinator coordinator) {
		this.coordinator = coordinator;
	}

	/**
	 * Coordinate the two phases on behalf of {@link #coordinator}, including
	 * <ul>
	 * <li> Collecting results from each phase and computing for the next one
	 * <li> Logging for each phase
	 * <ul>
	 */
	@Override
	protected boolean onAdvance(int phase, int registeredParties) {
		Coordinator coord = (Coordinator) this.coordinator;
		
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
			coord.is_committed = coord.to_commit_decision && coord.committed_decisions.values().stream().allMatch(decision -> decision);
			return true;	// this phaser has finished its job.
			
		default:
			return true;	
		}
	}
}
