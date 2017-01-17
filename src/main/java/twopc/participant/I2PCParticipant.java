package twopc.participant;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import exception.transaction.TransactionExecutionException;
import kvs.component.Timestamp;
import twopc.PreparedResult;

/**
 * Interface {@link I2PCParticipant} exposes 
 * {@link #prepare(ToCommitTransaction, VersionConstraintManager)}
 * and {@link #commit(ToCommitTransaction, Timestamp)} for participants of 2PC protocol.
 * These operations are available remotely, by letting this interface
 * extend {@link Remote}.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface I2PCParticipant extends Remote {
    /**
     * The first phase of 2PC protocol.
     * @param tx {@link ToCommitTransaction} to commit
     * @param vcm {@link VersionConstraintManager} associated with {@code tx}
     * @return {@code true} if this {@link I2PCParticipant} is ready to commit {@code tx};
     *  {@code false}, otherwise.
     * @throws RemoteException
     * @throws TransactionExecutionException
     */
	PreparedResult prepare(ToCommitTransaction tx, VersionConstraintManager vcm)
            throws RemoteException, TransactionExecutionException;

    /**
     * The second phase of 2PC protocol: the commit case
     * @param tx {@link ToCommitTransaction} to commit
     * @param cts commit timestamp
     * @return {@code true} if committed successfully; {@code false}, otherwise.
     * @throws RemoteException
     * @throws TransactionExecutionException
     */
	boolean commit(ToCommitTransaction tx, Timestamp cts)
            throws RemoteException, TransactionExecutionException;

    /**
     * The second phase of 2PC protocol: the abort case
     * @param tx  {@link ToCommitTransaction} aborted
     * @throws RemoteException
     * @throws TransactionExecutionException
     */
    void abort(ToCommitTransaction tx)
            throws RemoteException, TransactionExecutionException;
}
