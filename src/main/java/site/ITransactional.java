package site;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import exception.transaction.TransactionExecutionException;
import kvs.component.Timestamp;

/**
 * Interface {@link ITransactional} exposes transactional processing
 * specific operations {@link #start()} and 
 * {@link #commit(ToCommitTransaction, VersionConstraintManager)}.
 * These operations are available remotely, by letting this interface
 * {@link ITransactional} extend {@link Remote}.
 * @author hengxin
 * @date Created on Jan 9, 2016
 */
public interface ITransactional extends Remote {

	/**
	 * Start a transaction.
	 * @return	start-timestamp of the transaction to start
	 * @throws RemoteException	if an error occurs during RMI
	 * @throws TransactionExecutionException	if an error occurs during the transactional start operation
	 */
    Timestamp start()
            throws RemoteException, TransactionExecutionException;
	
	/**
	 * To commit a transaction.
	 * @param tx			transaction to commit
	 * @param vc_manager	version constraints associated with @param tx
	 * @return				{@code true} if the transaction commits successfully; {@code false}, otherwise.
	 * @throws RemoteException	if an error occurs during RMI
	 * @throws TransactionExecutionException	if an error occurs during the transactional commit operation
	 */
    boolean commit(ToCommitTransaction tx, VersionConstraintManager vc_manager)
            throws RemoteException, TransactionExecutionException;
}
