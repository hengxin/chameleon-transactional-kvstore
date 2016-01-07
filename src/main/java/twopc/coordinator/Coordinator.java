package twopc.coordinator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

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
public final class Coordinator implements ICoordinator {

	private final ExecutorService exec = Executors.newCachedThreadPool();
	
	protected final Phaser phaser;

	private final VersionConstraintManager vcm;
	private final AbstractClientContext ctx;

	private final Map<ISite, ToCommitTransaction> site_tx_map;

	/**
	 * {@link #prepared_decisions} and {@link #committed_decisions}:
	 * Shared states among the coordinator and the participants.
	 * They collect the decisions of all the participants during the "PREPARE" phase 
	 * and the "COMMIT" phase, respectively.
	 * 
	 * @see	#to_commit_decision
	 * @see #is_committed
	 */
	protected final Map<ISite, Boolean> prepared_decisions = new ConcurrentHashMap<>();
	protected final Map<ISite, Boolean> committed_decisions = new ConcurrentHashMap<>();

	/**
	 * {@link #to_commit_decision}: 
	 * Shared state among the coordinator and the participants.
	 * It indicates the commit/abort decision for the "COMMIT" phase, and is computed 
	 * based on {@link #prepared_decisions}.
	 * 
	 * @see #prepared_decisions
	 */
	protected volatile boolean to_commit_decision = false;
	
	/**
	 * {@link #is_committed}:
	 * Shared state among the coordinator and the participants.
	 * It indicates the final committed/aborted state of the transaction, 
	 * and it is computed based on both {@link #to_commit_decision} 
	 * and {@link #committed_decisions}.
	 * 
	 * @see #to_commit_decision
	 * @see #committed_decisions
	 */
	protected volatile boolean is_committed = false;

	/**
	 * @param tx	transaction to commit
	 * @param vcm	manager of version constraint associated with @param tx
	 * @param ctx	client context 
	 */
	public Coordinator(final ToCommitTransaction tx, final VersionConstraintManager vcm, final AbstractClientContext ctx) {
		this.vcm = vcm;
		this.ctx = ctx;
		this.site_tx_map = this.ctx.partition(tx);

		this.phaser = new CommitPhaser(this);	// FIXME circular ref???
	}

	@Override
	public boolean execute2PC() {
		this.site_tx_map.keySet().stream()
			.forEach(site ->
						exec.submit(new CommitPhaserTask(this, (IParticipant) site, this.site_tx_map.get(site), this.vcm)));	// TODO the third parameter: the actual ToCommitTransaction
		
		// TODO the return value
		return false;
	}
}
