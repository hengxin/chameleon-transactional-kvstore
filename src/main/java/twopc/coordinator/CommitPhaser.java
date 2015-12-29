package twopc.coordinator;

import java.util.Arrays;
import java.util.concurrent.Phaser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2PC protocol proceeds in two phases:
 * the "prepare" phase and the "commit/abort" phase.
 * 
 * @author hengxin
 * @date Created on Dec 28, 2015
 * 
 * @implNote
 * {@link Phaser} introduced since Java 7 is intended 
 * for implementations of phase-based protocols. 
 */
public class CommitPhaser extends Phaser
{
	private final static Logger LOGGER = LoggerFactory.getLogger(CommitPhaser.class);
	
	public enum Phase { PREPARE, COMMIT, ABORT }
	
	private final ICoordinator coordinator;
	
	public CommitPhaser(ICoordinator coordinator)
	{
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
	protected boolean onAdvance(int phase, int registeredParties)
	{
		Coordinator coord = (Coordinator) this.coordinator;
		
		switch (phase)
		{
		case 0:
			LOGGER.info("All [{}] masters have been finished the [{}] phase.", registeredParties, Phase.PREPARE);
			
			// check the decisions of all participants during the PREPARE phase and determine whether to commit or abort the transaction
			coord.committed = Arrays.stream(coord.prepared_decisions).parallel().allMatch(decision -> decision.get());

			LOGGER.info("The commit/abort decision for the [{}] phase is [{}].", Phase.COMMIT, coord.committed);
			return false;	// not yet finished

		case 1:
			LOGGER.info("All [{}] masters have been finished the [{}] phase.", registeredParties, Phase.COMMIT); 

			// FIXME compute the return value
			// check the decisions of all participants during the COMMIT phase and compute the return value
			coord.committed = Arrays.stream(coord.prepared_decisions).parallel().allMatch(decision -> decision.get());
			return true;	// phaser has finished its job.
			
		default:
			return true;	
		}
	}
}
