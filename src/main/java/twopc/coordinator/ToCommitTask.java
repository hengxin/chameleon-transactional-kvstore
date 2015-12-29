package twopc.coordinator;

import java.util.concurrent.Callable;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import twopc.coordinator.CommitPhaser.Phase;
import twopc.participant.IParticipant;

/**
 * Two-phase commit protocol consists of "prepare" and "commit".
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public class ToCommitTask implements Callable<Boolean>
{
	private final static Logger LOGGER = LoggerFactory.getLogger(ToCommitTask.class);
	
	private final Coordinator coordinator;
	private final int id;
	private final Phaser phaser;
	private final IParticipant participant;
	private final ToCommitTransaction tx;
	private final VersionConstraintManager vcm;

	public ToCommitTask(final Coordinator coordinator,
			final int id, final Phaser phaser, final IParticipant participant, 
			final ToCommitTransaction tx, final VersionConstraintManager vcm)
	{
		this.coordinator = coordinator;
		
		this.id = id;
		this.phaser = phaser;
		this.participant = participant;

		this.tx = tx;
		this.vcm = vcm;
	}

	@Override
	public Boolean call() throws Exception
	{
		LOGGER.info("Begin the [{}] phase with participant [{}].", Phase.PREPARE, this.participant);
		// call prepare() at this participant
		this.coordinator.prepared_decisions[this.id].set(false);	// TODO to assign the actual value
		phaser.arriveAndAwaitAdvance();
		
		LOGGER.info("Begin the [{}] phase with participant [{}].", Phase.COMMIT, this.participant);
		// call commit() at this participant
		this.coordinator.prepared_decisions[this.id].set(false);	// TODO to assign the actual value
		phaser.arriveAndAwaitAdvance();

		return null;
	}
}