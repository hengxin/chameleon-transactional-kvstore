/**
 * 
 */
package kvs.table;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

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
public class MultiTimestampedCellsStore implements ITimestampedCellStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiTimestampedCellsStore.class);

    private final static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(runnable -> {
			Thread thread = Executors.defaultThreadFactory().newThread(runnable);
			thread.setName("GC Daemon");
			thread.setDaemon(true);
		    return thread;});
	
	// TODO consider other data structures (how do real databases implement this?)
	private final ConcurrentSkipListSet<ITimestampedCell> tsCells = new ConcurrentSkipListSet<>();
	
	public MultiTimestampedCellsStore() {}
	
	/**
	 * @implNote
	 * The <code>put</code> semantics is to add the element <i>if</i> it is not already present.
	 * This works as expected because {@link MultiTimestampedCellsStore} maintains
	 * multi-timestamped data and is not intended to replace some existing data. 
	 */
	@Override
	public void put(ITimestampedCell tsCell) {
//		Assert.assertTrue("It is not intended to replace an existing data.", ! tsCells.contains(tsCell));
        LOGGER.debug("Begin: MultiTimestampedCellsStore adds tsCell [{}].", tsCell);
        LOGGER.debug("MultiTimestampedCellsStore adds tsCell [{}].", tsCell);
		tsCells.add(tsCell);
        LOGGER.debug("End: MultiTimestampedCellsStore adds tsCell [{}].", tsCell);
	}

	@Override
	public ITimestampedCell get(Timestamp ts) {
		// TODO floor (<=) or lower (<)?
//        LOGGER.debug("tsCells is [{}] and ts to lookup is [{}].", tsCells, ts);
		ITimestampedCell tsCell =  tsCells.floor(new TimestampedCell(ts, Cell.CELL_INIT));
        if (tsCell == null)
            tsCell = TimestampedCell.TIMESTAMPED_CELL_INIT;   // FIXME: only for debug
        return tsCell;
	}

	@Override
	public ITimestampedCell get() { return tsCells.last(); }

	/**
	 * This {@link String} format does not necessarily reflect all elements.
	 */
	@NotNull
    @Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("TimestampedCells", tsCells)
				.toString();
	}

	/**
	 * GC to avoid OOM.
	 * 
	 * @implNote
	 * It only guarantees that the size of {@link #tsCells} is less than
	 * {@value MasterConfig#TABLE_CAPACITY}, in the use cases where
	 * no other methods would explicitly remove elements from {@link #tsCells}.
	 */
	@Override
	public void startGCDaemon() {
        exec.scheduleWithFixedDelay(
                () -> IntStream.range(0, tsCells.size() - MasterConfig.TABLE_CAPACITY)
                        .forEach(i -> tsCells.pollFirst()),
                5,
                5,
                TimeUnit.SECONDS);
	}
}
