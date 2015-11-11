package kvs.table;

import kvs.compound.ITimestampedCell;

/**
 * @author hengxin
 * @date Created on 11-11-2015
 * 
 * Tables in master sites which use {@link MultiTimestampedCellsStore}.
 */
public class MasterTable extends AbstractTable
{
	@Override
	public ITimestampedCellStore initStore(ITimestampedCell tc)
	{
		return new MultiTimestampedCellsStore(tc);
	}
}
