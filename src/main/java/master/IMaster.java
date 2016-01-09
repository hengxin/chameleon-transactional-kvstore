package master;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import exception.transaction.TransactionExecutionException;
import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date Created on 10-27-2015
 */
@Deprecated
public interface IMaster extends Remote {
	public Timestamp start() throws RemoteException, TransactionExecutionException;
	public boolean commit(ToCommitTransaction tx, VersionConstraintManager vc_manager) throws RemoteException;
}
