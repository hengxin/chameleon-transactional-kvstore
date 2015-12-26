package kvs.table;

import kvs.compound.ITimestampedCell;

/**
 * @author hengxin
 * @date Created on 11-11-2015
 * 
 * Table in slave sites; it uses {@link SingleTimestampedCellStore}.
 */
public class SlaveTable extends AbstractTable
{
	@Override
	public ITimestampedCellStore initStore(ITimestampedCell ts_cell)
	{
		return new SingleTimestampedCellStore(ts_cell);
	}

}
