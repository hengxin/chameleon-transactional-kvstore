package twopc.participant;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import exception.transaction.TransactionExecutionException;
import kvs.component.Timestamp;

/**
 * Interface {@link I2PCParticipant} exposes 
 * {@link #prepare(ToCommitTransaction, VersionConstraintManager)}
 * and {@link #complete()} for participants of 2PC protocol.
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
	boolean prepare(ToCommitTransaction tx, VersionConstraintManager vcm)
            throws RemoteException, TransactionExecutionException;

    /**
     * The second phase of 2PC protocol.
     * @param ca to commit ({@code true}) or to abort ({@code false})
     * @param tx {@link ToCommitTransaction} to commit if ca is {@code true}
     * @param cts commit timestamp
     * @return {@code true} if committed successfully; {@code false}, otherwise.
     * @throws RemoteException
     * @throws TransactionExecutionException
     */
	boolean complete(boolean ca, ToCommitTransaction tx, Timestamp cts)
            throws RemoteException, TransactionExecutionException;
}
