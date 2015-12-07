package master;

import java.net.NoRouteToHostException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import client.clientlibrary.transaction.txexception.TransactionException;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date Created on 10-27-2015
 */
public interface IMaster extends Remote
{
	public Timestamp start() throws RemoteException, NoRouteToHostException, TransactionException;
	public Cell read(Row row, Column col) throws RemoteException;
	public boolean commit(ToCommitTransaction tx, VersionConstraintManager vc_manager) throws RemoteException;
}
