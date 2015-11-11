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
	 * put an {@ITimestampedCell} into this {@link ITimestampedCellStore}
	 * @param ts_cell an {@link ITimestampedCell} to put
	 */
	public void put(ITimestampedCell ts_cell);

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
