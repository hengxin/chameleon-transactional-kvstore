package twopc.coordinator;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import site.ISite;
import twopc.coordinator.RVSIBasic2PCCoordinator.Phase;
import twopc.participant.IParticipant;

/**
 * {@link CommitPhaserTask} executes the 2PC protocols with a single participant.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public final class CommitPhaserTask implements Callable<Boolean> {

	private final static Logger LOGGER = LoggerFactory.getLogger(CommitPhaserTask.class);
	
	private final AbstractCoordinator coordinator;
	private final IParticipant participant;

	private final ToCommitTransaction tx;
	private final VersionConstraintManager vcm;

	/**
	 * @param coordinator	{@link AbstractCoordinator} of this task
	 * @param participant	{@link IParticipant} of this task; it executes this task.
	 * @param tx			{@link ToCommitTransaction} to process in this task
	 * @param vcm			{@link VersionConstraintManager} associated with @param tx
	 * @deprecated	FIXME redesign @param vcm
	 */
	@Deprecated
	public CommitPhaserTask(final AbstractCoordinator coordinator,
							final IParticipant participant, 
							final ToCommitTransaction tx, 
							final VersionConstraintManager vcm) {
		this.coordinator = coordinator;
		this.participant = participant;

		this.tx = tx;
		this.vcm = vcm;
	}

	@Override
	public Boolean call() throws Exception {
		LOGGER.info("Begin the [{}] phase with participant [{}].", Phase.PREPARE, this.participant);
		boolean prepared_decision = this.participant.prepare2PC(tx, vcm);
		this.coordinator.prepared_decisions.put((ISite) participant, prepared_decision);
		((RVSIBasic2PCCoordinator) this.coordinator).phaser.arriveAndAwaitAdvance();
		
		LOGGER.info("Begin the [{}] phase with participant [{}].", Phase.COMMIT, this.participant);
		boolean committed_decision = this.participant.commit2PC();
		this.coordinator.committed_decisions.put((ISite) participant, committed_decision);
		((RVSIBasic2PCCoordinator) this.coordinator).phaser.arriveAndAwaitAdvance();

		return null;
	}
}