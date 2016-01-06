package site;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import exception.transaction.TransactionExecutionException;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;

/**
 * An {@link ISite} is a server which supports transactional operations
 * and can be remotely accessed.
 *
 * @author hengxin
 * @date Created on 12-09-2015
 */
public interface ISite extends Remote {

	public ITimestampedCell read(Row r, Column c) throws RemoteException;

	public Timestamp start() throws RemoteException, TransactionExecutionException;
	public boolean commit(ToCommitTransaction tx, VersionConstraintManager vc_manager) throws RemoteException, TransactionExecutionException;
}
