package kvs.table;

import com.google.common.base.MoreObjects;

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
public class SingleTimestampedCellStore implements ITimestampedCellStore
{
	private volatile ITimestampedCell single_ts_cell;
	
	/**
	 * Default constructor: initialize this store with {@value TimestampedCell#TIMESTAMPED_CELL_INIT}
	 */
	public SingleTimestampedCellStore() 
	{
		this.single_ts_cell = TimestampedCell.TIMESTAMPED_CELL_INIT;
	}

	/**
	 * Constructor: initialize this store with @param ts_cell
	 * @param ts_cell an {@link ITimestampedCell}
	 */
	public SingleTimestampedCellStore(ITimestampedCell ts_cell)
	{
		this.single_ts_cell = ts_cell;
	}

	@Override
	public void put(ITimestampedCell ts_cell)
	{
		this.single_ts_cell = ts_cell;
	}

	/**
	 * There is only a single data version.
	 * Ignore the {@link Timestamp} parameter.
	 */
	@Override
	public ITimestampedCell get(Timestamp ts)
	{
		return this.get();
	}

	@Override
	public ITimestampedCell get()
	{
		return this.single_ts_cell;
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.single_ts_cell)
				.toString();
	}

	/**
	 * @throws {@link UnspportedOperationException}
	 */
	@Override
	public void startGCDaemon()
	{
		throw new UnsupportedOperationException("GC is not supported for SingleTimestampedStore.");
	}
}
