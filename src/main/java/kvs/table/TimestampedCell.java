/**
 * 
 */
package kvs.table;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * Implement {@link ITimestampedCell} using {@link ConcurrentSkipListMap}
 */
public class TimestampedCell implements ITimestampedCell
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
	public Cell getLatest(Timestamp ts)
	{
		return this.ts_cells.floorEntry(ts).getValue();
	}

}
