/**
 * 
 */
package kvs.table;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;

import com.google.common.base.MoreObjects;

import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;
import master.MasterConfig;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * Implements the interface {@link ITimestampedCellStore} using {@link ConcurrentSkipListSet}.
 * It maintains {@link ITimestampedCell}s in their timestamp-order.
 */
public class MultiTimestampedCellsStore implements ITimestampedCellStore
{
	private final static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(runnable -> {
			Thread thread = Executors.defaultThreadFactory().newThread(runnable);
			thread.setName("GC Daemon");
			thread.setDaemon(true);
		    return thread;});
	
	// TODO consider other data structures (how do real databases implement this?)
	private final ConcurrentSkipListSet<ITimestampedCell> ts_cells = new ConcurrentSkipListSet<>();
	
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

	/**
	 * This {@link String} format does not necessarily reflect all elements.
	 */
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("TimestampedCells", this.ts_cells)
				.toString();
	}

	/**
	 * GC to avoid OOM.
	 * 
	 * @implNote
	 * It only guarantees that the size of {@link #ts_cells} is greater than
	 * or equal to {@value MasterConfig#TABLE_CAPACITY}, in the use cases where
	 * no other methods would explicitly remove elements from {@link #ts_cells}.
	 */
	@Override
	public void startGCDaemon()
	{
		if(MasterConfig.TABLE_CAPACITY < Integer.MAX_VALUE)
		{	
			exec.scheduleWithFixedDelay(
				() -> {
					int surplus = this.ts_cells.size() - MasterConfig.TABLE_CAPACITY;
					for(int i = 0; i < surplus; i++)
						this.ts_cells.pollFirst();
					}, 
				5, 
				5, 
				TimeUnit.SECONDS);
		}
	}
}
