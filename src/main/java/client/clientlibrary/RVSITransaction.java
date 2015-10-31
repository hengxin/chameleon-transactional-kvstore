/**
 * 
 */
package client.clientlibrary;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import client.communication.ClientContacts;
import kvs.table.Cell;
import kvs.table.Column;
import kvs.table.Row;
import master.SIMaster;

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

	private List<Update> updates = new ArrayList<>();	// to buffer write operations
	
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
		this.updates.add(new Update(r, c, data));
		return true;
	}

	/* 
	 * @see client.clientlibrary.Transaction#end()
	 */
	@Override
	public boolean end()
	{
		// TODO Auto-generated method stub
		return false;
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
	}
	
	/**
	 * @author hengxin
	 * @date 10-27-2015
	 * 
	 * update records for write operations in transactions
	 */
	public class Update
	{
		private final Row row;
		private final Column col;
		private final Cell data;

		public Update(Row row, Column col, Cell data)
		{
			this.row = row;
			this.col = col;
			this.data = data;
		}

		public Row getRow()
		{
			return row;
		}

		public Column getCol()
		{
			return col;
		}

		public Cell getData()
		{
			return data;
		}
	};
}
