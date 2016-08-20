package twopc.coordinator;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import client.context.AbstractClientContext;
import exception.transaction.TransactionExecutionException;
import kvs.component.Timestamp;
import rmi.IRMI;
import twopc.participant.I2PCParticipant;

/**
 * {@link Abstract2PCCoordinator} is responsible for coordinating the 2PC protocol.
 * It maintains the basic states of the traditional 2PC mechanism,
 * and aims to support different variants of 2PC protocol.
 * <p> The client who issues the transaction to commit plays the role of coordinator.
 * @author hengxin
 * @date Created on Jan 7, 2016
 */
public abstract class Abstract2PCCoordinator implements Remote, IRMI, Serializable {

	/**
	 * {@link #prepared_decisions} and {@link #committed_decisions}:
	 * Shared states among the coordinator and the participants.
	 * They collect the decisions of all the participants during the "PREPARE" phase 
	 * and the "COMMIT" phase, respectively.
	 * 
	 * @see	#to_commit_decision
	 * @see #is_committed
	 */
	protected transient final Map<I2PCParticipant, Boolean> prepared_decisions = new ConcurrentHashMap<>();
	protected transient final Map<I2PCParticipant, Boolean> committed_decisions = new ConcurrentHashMap<>();

	/**
	 * {@link #to_commit_decision}: 
	 * Shared state among the coordinator and the participants.
	 * It indicates the commit/abort decision for the "COMMIT" phase, and is computed 
	 * based on {@link #prepared_decisions}.
	 * 
	 * @see #prepared_decisions
	 */
	protected transient volatile boolean to_commit_decision = false;
	
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
	protected transient volatile boolean is_committed = false;
	
	protected final AbstractClientContext cctx;
    protected transient Timestamp cts;  // commit timestamp

    /**
	 * @param ctx	context for this coordinator;
     *              it provides information about the sites and the partition strategy.
	 */
	public Abstract2PCCoordinator(@NotNull AbstractClientContext ctx) { cctx = ctx; }

	/**
	 * The coordinator executes 2PC protocol.
	 * @param tx	transaction to commit
	 * @return {@code true} if 2PC protocol succeeds in committing; {@code false}, otherwise.
     * @throws RemoteException thrown if errors occur in remote accesses
	 */
	public abstract boolean execute2PC(final ToCommitTransaction tx, final VersionConstraintManager vcm)
            throws RemoteException, TransactionExecutionException;

    public abstract boolean onPreparePhaseFinished();
    public abstract boolean onCommitPhaseFinished();

    @Override
    public void export() {
        // TODO
    }

    @Override
    public void reclaim() {

    }
}
