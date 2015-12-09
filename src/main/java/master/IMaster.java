package master;

import java.net.NoRouteToHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import exception.TransactionException;
import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date Created on 10-27-2015
 */
public interface IMaster extends Remote
{
	public Timestamp start() throws RemoteException, NoRouteToHostException, TransactionException;
	public boolean commit(ToCommitTransaction tx, VersionConstraintManager vc_manager) throws RemoteException;
}
