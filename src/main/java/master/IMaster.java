package master;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.BufferedUpdates;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * Executing transactions at master using a local SI protocol
 */
public interface IMaster extends Remote
{
	public Timestamp start() throws RemoteException, InterruptedException, ExecutionException;
	public Cell read(Row row, Column col) throws RemoteException;
	public boolean commit(BufferedUpdates updates, VersionConstraintManager vc_manager) throws RemoteException;
}
