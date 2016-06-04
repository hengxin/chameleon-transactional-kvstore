package twopc.coordinator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Phaser;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import twopc.coordinator.phaser.CommitPhaser;
import twopc.participant.I2PCParticipant;

/**
 * {@link CommitPhaserTask} executes the 2PC protocols with a single participant.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public final class CommitPhaserTask implements Callable<Boolean> {

	private final static Logger LOGGER = LoggerFactory.getLogger(CommitPhaserTask.class);
	
	private final Abstract2PCCoordinator coord;
	private final I2PCParticipant participant;
    private final Phaser phaser;

	private final ToCommitTransaction tx;
	private final VersionConstraintManager vcm;

	/**
	 * @param coord	{@link Abstract2PCCoordinator} of this task
	 * @param participant	{@link I2PCParticipant} of this task who executes this task.
	 * @param tx			{@link ToCommitTransaction} to process in this task
	 * @param vcm			{@link VersionConstraintManager} associated with @param tx
	 * FIXME redesign @param vcm
	 */
	public CommitPhaserTask(final Abstract2PCCoordinator coord,
							final I2PCParticipant participant, 
							final ToCommitTransaction tx, 
							final VersionConstraintManager vcm) {
		this.coord = coord;
		this.participant = participant;
        this.phaser = ((RVSI2PCPhaserCoordinator) coord).phaser;
        this.phaser.register();

		this.tx = tx;
		this.vcm = vcm;
	}

	@Override
	public Boolean call() throws Exception {
		LOGGER.info("Begin the [{}] phase with participant [{}].", CommitPhaser.Phase.PREPARE, this.participant);
        boolean prepared_decision = participant.prepare(tx, vcm);
        coord.prepared_decisions.put(participant, prepared_decision);
        phaser.arriveAndAwaitAdvance();

		LOGGER.info("Begin the [{}] phase with participant [{}].", CommitPhaser.Phase.COMMIT, this.participant);
        boolean committed_decision = participant.complete();
        phaser.arriveAndAwaitAdvance();
        coord.committed_decisions.put(participant, committed_decision);

		return true;
	}
}