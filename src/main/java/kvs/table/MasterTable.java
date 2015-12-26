package kvs.table;

/**
 * @author hengxin
 * @date Created on 11-11-2015
 * 
 * <p> Tables in master sites which use {@link MultiTimestampedCellsStore}.
 */
public class MasterTable extends AbstractTable
{
	@Override
	public ITimestampedCellStore create()
	{
		return new MultiTimestampedCellsStore();
	}
}
