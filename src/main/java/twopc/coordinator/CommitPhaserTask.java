package twopc.coordinator;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Phaser;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import twopc.participant.I2PCParticipant;

import static twopc.coordinator.phaser.CommitPhaser.Phase.ABORT;
import static twopc.coordinator.phaser.CommitPhaser.Phase.COMMIT;
import static twopc.coordinator.phaser.CommitPhaser.Phase.PREPARE;

/**
 * {@link CommitPhaserTask} executes the 2PC protocols with a single participant.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public final class CommitPhaserTask implements Callable<Boolean> {
	private final static Logger LOGGER = LoggerFactory.getLogger(CommitPhaserTask.class);
	
	@NotNull
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
	public CommitPhaserTask(@NotNull final Abstract2PCCoordinator coord,
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
		LOGGER.info("The Coord [{}] begins the [{}] phase with participant [{}].",
                coord, PREPARE, participant);

        boolean preparedDecision = participant.prepare(tx, vcm);
        coord.preparedDecisions.put(participant, preparedDecision);

        phaser.arriveAndAwaitAdvance();

        if (coord.toCommitDecision) { // commit case of the second phase of 2PC protocol
            LOGGER.info("The Coord [{}] begins the [{}] phase with participant [{}].",
                    coord, COMMIT, participant);

            boolean committedDecision = participant.commit(tx, coord.cts);
            coord.committedDecisions.put(participant, committedDecision);
        } else { // abort case of the second phase of 2PC protocol
            LOGGER.info("Begin the [{}] phase with participant [{}].",
                    ABORT, participant);

            participant.abort(tx);
        }

        phaser.arriveAndAwaitAdvance();

		return true;
	}

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(participant)
                .addValue(tx)
                .addValue(vcm)
                .toString();
    }
}