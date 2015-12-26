package kvs.table;

/**
 * @author hengxin
 * @date Created on 11-11-2015
 * 
 * Table in slave sites; it uses {@link SingleTimestampedCellStore}.
 */
public class SlaveTable extends AbstractTable
{
	@Override
	public ITimestampedCellStore create()
	{
		return new SingleTimestampedCellStore();
	}

}
