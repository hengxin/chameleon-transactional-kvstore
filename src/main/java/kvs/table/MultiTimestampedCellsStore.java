/**
 * 
 */
package kvs.table;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * Implements the interface {@link ITimestampedCellStore} using {@link ConcurrentSkipListMap}.
 * It maintains multiple {@link ITimestampedCell}s.
 */
public class MultiTimestampedCellsStore implements ITimestampedCellStore
{
	// TODO consider other data structures (how do real databases implement this?)
	private ConcurrentSkipListMap<Timestamp, Cell> ts_cells = new ConcurrentSkipListMap<>();
	
	/* 
	 * @see kvs.table.ITimestampedCell#update(kvs.table.Timestamp, kvs.table.Cell)
	 */
	@Override
	public void update(Timestamp ts, Cell c)
	{
		this.ts_cells.put(ts, c);
	}

	/* 
	 * @see kvs.table.ITimestampedCell#getLatest(kvs.table.Timestamp)
	 */
	@Override
	public ITimestampedCell get(Timestamp ts)
	{
		return this.ts_cells.floorEntry(ts);
	}

	/**
	 * @see kvs.table.ITimestampedCellStore#get()
	 */
	@Override
	public ITimestampedCell get()
	{
		return this.ts_cells.lastEntry();
	}

}
