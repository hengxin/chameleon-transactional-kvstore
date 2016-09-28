package twopc.coordinator.phaser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.Phaser;

import exception.transaction.TransactionEndException;
import twopc.coordinator.Abstract2PCCoordinator;
import twopc.coordinator.RVSI2PCPhaserCoordinator;

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
public final class CommitPhaser extends Phaser implements Serializable {
    private static final long serialVersionUID = -14446810211374850L;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommitPhaser.class);

    private Abstract2PCCoordinator coord;
    public enum Phase { PREPARE, COMMIT, ABORT }

    public CommitPhaser(Abstract2PCCoordinator coord) { this.coord = coord; }

    /**
     * Coordinate the two phases on behalf of the enclosing {@link RVSI2PCPhaserCoordinator}, including
     * <ul>
     * <li> Collecting results from each phase and computing for the next one
     * <li> Logging for each phase
     * <ul>
     *
     * @param phase {@inheritDoc}. There are two phases in the 2PC protocol.
     *                           So the value of @param phase here is either 0 or 1.
     * @param registeredParties {@inheritDoc}.
     *
     * @return {@code true} if both phases have been finished; {@code false}, otherwise.
     *
     * @throws IllegalArgumentException thrown if no such a phase exists.
     */
    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase) {
            case 0:
                LOGGER.debug("All [{}] masters have finished the [{}] phase.", getArrivedParties(), Phase.PREPARE);
                boolean toCommitDecision = false;
                try {
                    toCommitDecision = coord.onPreparePhaseFinished();
                } catch (TransactionEndException tee) {
                    LOGGER.error(tee.getMessage());
                }
                LOGGER.debug("The commit/abort decision for the [{}] phase is [{}].", Phase.COMMIT, toCommitDecision);

                return false;	// this phaser has not yet finished

            case 1:
                LOGGER.debug("All [{}] masters have finished the [{}] phase.", getArrivedParties(), Phase.COMMIT);
                boolean isCommitted = coord.onCommitPhaseFinished();
                LOGGER.debug("This 2PC protocol ends with [{}].", isCommitted);
                return true;	// this phaser has finished its job.

            default:
                throw new IllegalArgumentException(String.format("No Such a Phase: [%s]", phase));
        }
    }
}
