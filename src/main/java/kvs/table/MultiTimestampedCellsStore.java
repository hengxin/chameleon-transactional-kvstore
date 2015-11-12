/**
 * 
 */
package kvs.table;

import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;

import com.google.common.base.MoreObjects;

import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

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
	private SortedSet<ITimestampedCell> ts_cells = new ConcurrentSkipListSet<>();
	
	/**
	 * Default constructor: an empty store
	 */
	public MultiTimestampedCellsStore() {}
	
	/**
	 * Initialize the store with an {@link ITimestampedCell}
	 * @param ts_cell an {@link ITimestampedCell}
	 */
	public MultiTimestampedCellsStore(ITimestampedCell ts_cell)
	{
		this.put(ts_cell);
	}

	/* 
	 * @see kvs.table.ITimestampedCell#update(kvs.table.ITimestampedCell)
	 */
	@Override
	public void put(ITimestampedCell ts_cell)
	{
		this.ts_cells.add(ts_cell);
	}

	/* 
	 * @see kvs.table.ITimestampedCell#getLatest(kvs.table.Timestamp)
	 */
	@Override
	public ITimestampedCell get(Timestamp ts)
	{
		// TODO floor (<=) or lower (<)?
		return ((ConcurrentSkipListSet<ITimestampedCell>) this.ts_cells).floor(new TimestampedCell(ts, Cell.CELL_INIT));
	}

	/**
	 * @see kvs.table.ITimestampedCellStore#get()
	 */
	@Override
	public ITimestampedCell get()
	{
		return ((ConcurrentSkipListSet<ITimestampedCell>) this.ts_cells).last();
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("TimestampedCells", this.ts_cells)
				.toString();
	}
}