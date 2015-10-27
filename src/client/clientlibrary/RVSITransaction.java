/**
 * 
 */
package client.clientlibrary;

import java.util.ArrayList;
import java.util.List;

import kvs.table.Cell;
import kvs.table.Column;
import kvs.table.Row;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * Implement our RVSI transaction which is enhanced by rvsi specification.
 */
public class RVSITransaction implements Transaction
{
	private long sts = 0L;	// start-timestamp
	private long cts = 0L;	// commit-timestamp

	private List<Update> updates = new ArrayList<>();	// to buffer write operations
	
	/* 
	 * @see client.clientlibrary.Transaction#begin()
	 */
	@Override
	public void begin()
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see client.clientlibrary.Transaction#read(kvs.table.Row, kvs.table.Column)
	 */
	@Override
	public void read(Row r, Column c)
	{
		// TODO Auto-generated method stub

	}

	/* 
	 * @see client.clientlibrary.Transaction#write(kvs.table.Row, kvs.table.Column, kvs.table.Cell)
	 */
	@Override
	public void write(Row r, Column c, Cell data)
	{
		this.updates.add(new Update(r, c, data));
	}

	/* (non-Javadoc)
	 * @see client.clientlibrary.Transaction#end()
	 */
	@Override
	public void end()
	{
		// TODO Auto-generated method stub

	}

	public void setRVSISpecification()
	{
		
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
