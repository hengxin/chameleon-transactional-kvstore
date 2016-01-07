package twopc.coordinator;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import client.context.AbstractClientContext;
import site.ISite;
import twopc.participant.IParticipant;

/**
 * Coordinator of 2PC protocol.
 * <p>
 * The coordinator executes an optimized 2PC protocol
 * with "early commit notification".
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public final class Coordinator extends AbstractCoordinator {

	private static final Logger LOGGER = LoggerFactory.getLogger(Coordinator.class);
	
	private static final ExecutorService exec = Executors.newCachedThreadPool();
	
	private final VersionConstraintManager vcm;
	private final AbstractClientContext ctx;

	private final Map<ISite, ToCommitTransaction> site_tx_map;

	/**
	 * @param tx	transaction to commit
	 * @param vcm	manager of version constraint associated with @param tx
	 * @param ctx	client context 
	 */
	public Coordinator(final ToCommitTransaction tx, final VersionConstraintManager vcm, final AbstractClientContext ctx) {
		this.vcm = vcm;
		this.ctx = ctx;
		this.site_tx_map = this.ctx.partition(tx);

		super.phaser = new CommitPhaser(this);	// FIXME circular ref???
	}

	@Override
	public boolean execute2PC() {
		List<CommitPhaserTask> task_list = this.site_tx_map.entrySet().stream()
				.map(site_tx_entry -> {
					CommitPhaserTask task = new CommitPhaserTask(this, (IParticipant) site_tx_entry.getKey(), site_tx_entry.getValue(), this.vcm);
					this.phaser.register();
					return task;
					})
				.collect(toList());
		
		try {
			exec.invokeAll(task_list);
		} catch (InterruptedException ie) {
			LOGGER.error("2PC protocol has been interrupted unexpectedly.", ie);	// FIXME fault-handling???
		}

		// FIXME the return value
		return false;
	}
}
