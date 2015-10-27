package master;

import java.util.List;

import client.clientlibrary.RVSITransaction.Update;
import kvs.table.Cell;
import kvs.table.Column;
import kvs.table.Row;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * Master employs an MVCC protocol to locally implement SI isolation level.
 */
public class SIMaster implements IMaster
{

	@Override
	public void start()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Cell read(Row row, Column col)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean commit(List<Update> updates)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
