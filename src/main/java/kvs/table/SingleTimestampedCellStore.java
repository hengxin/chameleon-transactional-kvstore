package kvs.table;

import com.google.common.base.MoreObjects;

import net.jcip.annotations.ThreadSafe;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

/**
 * @author hengxin
 * @data Created on 11-10-2015
 * 
 * Implements the interface {@link ITimestampedCellStore}.
 * It maintains only a single {@link ITimestampedCell}.
 */
@ThreadSafe
public class SingleTimestampedCellStore implements ITimestampedCellStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(SingleTimestampedCellStore.class);

    @NotNull private final AtomicReference<ITimestampedCell> singleTsCell;
	
	/**
	 * Default constructor: initialize this store with {@link TimestampedCell#TIMESTAMPED_CELL_INIT}
	 */
	public SingleTimestampedCellStore() {
		singleTsCell = new AtomicReference<>(TimestampedCell.TIMESTAMPED_CELL_INIT);
	}

	/**
	 * Constructor: initialize this store with @param ts_cell
	 * @param tsCell an {@link ITimestampedCell}
	 */
	public SingleTimestampedCellStore(ITimestampedCell tsCell) {
		singleTsCell = new AtomicReference<>(tsCell);
	}

	/**
	 * Replace the current value <i>if</i> @param ts_cell is newer than 
	 * the value of {@link #singleTsCell}.
	 * @implNote
	 * This "if-greater-then-swap" semantics is implemented using 
	 * {@link AtomicReference#getAndUpdate(java.util.function.UnaryOperator)}.
	 * See <a href = "http://stackoverflow.com/a/27347133/1833118">Greater-than compare-and-swap</a>
	 */
	@Override
	public void put(@NotNull ITimestampedCell tsCell) {
        LOGGER.debug("Slave receives [{}]. Is it newer than [{}]: [{}].",
                tsCell,
                singleTsCell.get(),
                singleTsCell.get().compareTo(tsCell) < 0);
		singleTsCell.getAndUpdate(x -> x.compareTo(tsCell) < 0 ? tsCell : x);
	}

	/**
	 * There is only a single data version.
	 * Ignore the {@link Timestamp} parameter.
	 */
	@Override
	public ITimestampedCell get(Timestamp ts) { return get(); }

	@Override
	public ITimestampedCell get() { return singleTsCell.get(); }

	@NotNull
    @Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(singleTsCell.get())
				.toString();
	}

	/**
	 * @throws {@link UnsupportedOperationException}
	 */
	@Override
	public void startGCDaemon() {
		throw new UnsupportedOperationException("GC is not supported for SingleTimestampedStore.");
	}

}
