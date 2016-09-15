package kvs.table;

import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * <p> Interface for data stores at sites (including the master and the slaves)
 */
public interface ITimestampedCellStore {
	/**
	 * put an {@ITimestampedCell} into this {@link ITimestampedCellStore}
	 * @param tsCell an {@link ITimestampedCell} to put
	 */
    void put(ITimestampedCell tsCell);

	/**
	 * lookup the latest preceding {@link ITimestampedCell} with smaller or equal {@link Timestamp} than @param ts
	 * @param ts {@link Timestamp}
	 * @return a {@link ITimestampedCell}
	 */
    ITimestampedCell get(Timestamp ts);
	
	/**
	 * lookup the <em>latest</em> {@link ITimestampedCell}
	 * @return a {@link ITimestampedCell}
	 */
    ITimestampedCell get();
	
	/**
	 * Start the garbage collector daemon thread.
	 */
    void startGCDaemon();
}
