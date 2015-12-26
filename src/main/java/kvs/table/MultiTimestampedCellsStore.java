/**
 * 
 */
package kvs.table;

import java.util.concurrent.ConcurrentSkipListSet;

import org.junit.Assert;

import com.beust.jcommander.Parameter;
import com.google.common.base.MoreObjects;

import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * Implements the interface {@link ITimestampedCellStore} using {@link ConcurrentSkipListSet}.
 * It maintains {@link ITimestampedCell}s in their timestamp-order.
 */
public class MultiTimestampedCellsStore implements ITimestampedCellStore
{
	// TODO to implement "fixed-capacity"
	@Parameter(names = "-capacity", description = "Number of Versions to Keep")
	private int capacity = 100;
	
	// TODO consider other data structures (how do real databases implement this?)
	private ConcurrentSkipListSet<ITimestampedCell> ts_cells = new ConcurrentSkipListSet<>();
	
	public MultiTimestampedCellsStore() {}
	
	/**
	 * @implNote
	 * The <code>put</code> semantics is to add the element <i>if</i> it is not already present.
	 * This works as expected because {@link MultiTimestampedCellsStore} maintains
	 * multi-timestamped data and is not intended to replace some existing data. 
	 */
	@Override
	public void put(ITimestampedCell ts_cell)
	{
		Assert.assertTrue("It is not intended to replace an existing data.", ! this.ts_cells.contains(ts_cell));
		this.ts_cells.add(ts_cell);
	}

	@Override
	public ITimestampedCell get(Timestamp ts)
	{
		// TODO floor (<=) or lower (<)?
		return this.ts_cells.floor(new TimestampedCell(ts, Cell.CELL_INIT));
	}

	@Override
	public ITimestampedCell get()
	{
		return this.ts_cells.last();
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("TimestampedCells", this.ts_cells)
				.toString();
	}
}
