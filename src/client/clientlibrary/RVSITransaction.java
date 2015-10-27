/**
 * 
 */
package client.clientlibrary;

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

	/* (non-Javadoc)
	 * @see client.clientlibrary.Transaction#write(kvs.table.Row, kvs.table.Column, kvs.table.Cell)
	 */
	@Override
	public void write(Row r, Column c, Cell data)
	{
		// TODO Auto-generated method stub

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
}
