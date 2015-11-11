package kvs.table;

import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * Interface for data stores at sites (including the master and the slaves)
 */
public interface ITimestampedCellStore
{
	/**
	 * update this store with an {@ITimestampedCell}
	 * @param ts_cell {@link ITimestampedCell}
	 */
	public void update(ITimestampedCell ts_cell);

	/**
	 * get the latest preceding {@link ITimestampedCell} with smaller or equal {@link Timestamp} than @param ts 
	 * @param ts {@link Timestamp}
	 * @return a {@link ITimestampedCell}
	 */
	public ITimestampedCell get(Timestamp ts);
	
	/**
	 * get the latest {@link ITimestampedCell}
	 * @return a {@link ITimestampedCell}
	 */
	public ITimestampedCell get();
}
