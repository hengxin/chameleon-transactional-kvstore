package twopc.coordinator;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import rmi.IRemoteSite;
import twopc.coordinator.RVSI2PCPhaserCoordinator.Phase;
import twopc.participant.I2PCParticipant;

/**
 * {@link CommitPhaserTask} executes the 2PC protocols with a single participant.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public final class CommitPhaserTask implements Callable<Boolean> {

	private final static Logger LOGGER = LoggerFactory.getLogger(CommitPhaserTask.class);
	
	private final Abstract2PCCoordinator coordinator;
	private final I2PCParticipant participant;

	private final ToCommitTransaction tx;
	private final VersionConstraintManager vcm;

	/**
	 * @param coordinator	{@link Abstract2PCCoordinator} of this task
	 * @param participant	{@link I2PCParticipant} of this task; it executes this task.
	 * @param tx			{@link ToCommitTransaction} to process in this task
	 * @param vcm			{@link VersionConstraintManager} associated with @param tx
	 * @deprecated	FIXME redesign @param vcm
	 */
	@Deprecated
	public CommitPhaserTask(final Abstract2PCCoordinator coordinator,
							final I2PCParticipant participant, 
							final ToCommitTransaction tx, 
							final VersionConstraintManager vcm) {
		this.coordinator = coordinator;
		this.participant = participant;

		this.tx = tx;
		this.vcm = vcm;
		
		((RVSI2PCPhaserCoordinator) this.coordinator).phaser.register();
	}

	@Override
	public Boolean call() throws Exception {
		LOGGER.info("Begin the [{}] phase with participant [{}].", Phase.PREPARE, this.participant);
		boolean prepared_decision = this.participant.prepare(tx, vcm);
		this.coordinator.prepared_decisions.put((IRemoteSite) participant, prepared_decision);
		((RVSI2PCPhaserCoordinator) this.coordinator).phaser.arriveAndAwaitAdvance();
		
		LOGGER.info("Begin the [{}] phase with participant [{}].", Phase.COMMIT, this.participant);
		boolean committed_decision = this.participant.complete();
		this.coordinator.committed_decisions.put((IRemoteSite) participant, committed_decision);
		((RVSI2PCPhaserCoordinator) this.coordinator).phaser.arriveAndAwaitAdvance();

		return null;
	}
}