package twopc.coordinator;

import com.sun.istack.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import client.clientlibrary.transaction.ToCommitTransaction;
import client.context.AbstractClientContext;
import rmi.IRMI;

/**
 * {@link Abstract2PCCoordinator} is responsible for coordinating the 2PC protocol.
 * It maintains the basic states of the traditional 2PC mechanism,
 * and aims to support different variants of 2PC protocol.
 * <p> The client who issues the transaction to commit plays the role of coordinator.
 * @author hengxin
 * @date Created on Jan 7, 2016
 */
public abstract class Abstract2PCCoordinator {

	/**
	 * {@link #prepared_decisions} and {@link #committed_decisions}:
	 * Shared states among the coordinator and the participants.
	 * They collect the decisions of all the participants during the "PREPARE" phase 
	 * and the "COMMIT" phase, respectively.
	 * 
	 * @see	#to_commit_decision
	 * @see #is_committed
	 */
	protected final Map<IRMI, Boolean> prepared_decisions = new ConcurrentHashMap<>();
	protected final Map<IRMI, Boolean> committed_decisions = new ConcurrentHashMap<>();

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
	
	protected final AbstractClientContext ctx;
	
	/**
	 * @param ctx	context for this coordinator; it may provide information about the data-store 
	 * 	and the partition strategy. It can be {@code null}, if your coordinator does not need one.
	 */
	public Abstract2PCCoordinator(@Nullable AbstractClientContext ctx) {
		this.ctx = ctx;
	}
	
	/**
	 * The coordinator executes 2PC protocol.
	 * @param tx	transaction to commit
	 * @return {@code true} if 2PC protocol succeeds in committing; {@code false}, otherwise.
	 */
	public abstract boolean execute2PC(ToCommitTransaction tx);
}
