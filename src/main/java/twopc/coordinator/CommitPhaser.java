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
	
	private final Coordinator coordinator;
	
	public CommitPhaser(Coordinator coordinator)
	{
		this.coordinator = coordinator;
	}

	/**
	 * Logging for each phase.
	 */
	@Override
	protected boolean onAdvance(int phase, int registeredParties)
	{
		switch (phase)
		{
		case 0:
			LOGGER.info("All [{}] masters have been finished the [{}] phase.", registeredParties, Phase.PREPARE);
			
			// check the decisions of all participants during the PREPARE phase and determine whether to commit or abort the transaction
			coordinator.committed = Arrays.stream(coordinator.decisions).parallel().allMatch(decision -> decision.get());

			// reset the decisions for reuse in the later COMMIT phase
			Arrays.stream(coordinator.decisions).parallel().forEach(decision -> decision.set(false));
			
			LOGGER.info("The commit/abort decision for the [{}] phase is [{}].", Phase.COMMIT, coordinator.committed);
			return false;

		case 1:
			LOGGER.info("All [{}] masters have been finished the [{}] phase.", registeredParties, Phase.COMMIT); 
			return true;
			
		default:
			return true;	// TODO what is the meaning of the boolean return value???
		}
	}
}
