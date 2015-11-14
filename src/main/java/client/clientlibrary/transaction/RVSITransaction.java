/**
 * 
 */
package client.clientlibrary.transaction;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

import client.communication.ClientContacts;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * Implement our RVSI transaction which is enhanced by rvsi specification.
 */
public class RVSITransaction implements ITransaction
{
	private long sts = 0L;	// start-timestamp
	private long cts = 0L;	// commit-timestamp

	private BufferedUpdates buffered_updates = new BufferedUpdates();	// to buffer write operations
	private QueryResults query_results = new QueryResults();			// to store query results
	
	/* 
	 * @see client.clientlibrary.Transaction#begin()
	 */
	@Override
	public boolean begin()
	{
		try
		{
			this.sts = ClientContacts.INSTANCE.getRemote_master().start();
		} catch (InterruptedException | ExecutionException | RemoteException e)
		{
			// TODO: to remove
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/* 
	 * @see client.clientlibrary.Transaction#read(kvs.table.Row, kvs.table.Column)
	 */
	@Override
	public boolean read(Row r, Column c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * @see client.clientlibrary.Transaction#write(kvs.table.Row, kvs.table.Column, kvs.table.Cell)
	 */
	@Override
	public boolean write(Row r, Column c, Cell data)
	{
		this.buffered_updates.intoBuffer(r, c, data);
		return true;
	}

	/* 
	 * @see client.clientlibrary.Transaction#end()
	 */
	@Override
	public boolean end()
	{
		try
		{
			boolean success = ClientContacts.INSTANCE.getRemote_master().commit(this.buffered_updates);
			if(! success)
			{
				// TODO restart the transaction???
			}
		} catch (RemoteException re)
		{
			// TODO: to remove
			re.printStackTrace();
			return false;
		}

		return true;
	}

	public void setRVSISpecification()
	{
		
	}
	
	/**
	 * protected only for test
	 * @return {@link #sts}
	 */
	protected long getSts()
	{
		return this.sts;
	};
}
