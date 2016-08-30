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
import exception.transaction.TransactionEndException;
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
	 * {@link #preparedDecisions} and {@link #committedDecisions}:
	 * Shared states among the coordinator and the participants.
	 * They collect the decisions of all the participants during the "PREPARE" phase 
	 * and the "COMMIT" phase, respectively.
	 * 
	 * @see	#toCommitDecision
	 * @see #isCommitted
	 */
	protected final Map<I2PCParticipant, Boolean> preparedDecisions = new ConcurrentHashMap<>();
	protected final Map<I2PCParticipant, Boolean> committedDecisions = new ConcurrentHashMap<>();

	/**
	 * {@link #toCommitDecision}:
	 * Shared state among the coordinator and the participants.
	 * It indicates the commit/abort decision for the "COMMIT" phase, and is computed 
	 * based on {@link #preparedDecisions}.
	 * 
	 * @see #preparedDecisions
	 */
	protected volatile boolean toCommitDecision = false;
	
	/**
	 * {@link #isCommitted}:
	 * Shared state among the coordinator and the participants.
	 * It indicates the final committed/aborted state of the transaction,
	 * and it is computed based on both {@link #toCommitDecision}
	 * and {@link #committedDecisions}.
	 * 
	 * @see #toCommitDecision
	 * @see #committedDecisions
	 */
	protected volatile boolean isCommitted = false;
	
	protected final AbstractClientContext cctx;
    protected Timestamp cts;  // commit timestamp

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

    public abstract boolean onPreparePhaseFinished()
            throws TransactionEndException;
    public abstract boolean onCommitPhaseFinished();

    @Override
    public void export() {
        // TODO
    }

    @Override
    public void reclaim() {

    }
}
