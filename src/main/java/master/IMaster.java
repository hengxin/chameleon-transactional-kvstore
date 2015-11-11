package master;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import client.clientlibrary.transaction.RVSITransaction.Update;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * Executing transactions at master using a local SI protocol
 */
public interface IMaster extends Remote
{
	public long start() throws RemoteException, InterruptedException, ExecutionException;
	public Cell read(Row row, Column col) throws RemoteException;
	public boolean commit(List<Update> updates /** leaving version constraint blank now **/) throws RemoteException;
}
