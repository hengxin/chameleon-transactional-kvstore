package master;

import java.rmi.Remote;
import java.util.List;
import java.util.concurrent.ExecutionException;

import client.clientlibrary.RVSITransaction.Update;
import kvs.table.Cell;
import kvs.table.Column;
import kvs.table.Row;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * Executing transactions at master using a local SI protocol
 */
public interface IMaster extends Remote
{
	public long start() throws InterruptedException, ExecutionException;
	public Cell read(Row row, Column col);
	public boolean commit(List<Update> updates /** leaving version constraint blank now **/);
}
