package kvs.table;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
	private ITimestampedCell single_ts_cell = TimestampedCell.TIMESTAMPED_CELL_INIT;
	
	private final ReadWriteLock rw_lock = new ReentrantReadWriteLock(true);
	private final Lock rl = rw_lock.readLock();
	private final Lock wl = rw_lock.writeLock();

	/**
	 * Default constructor: initialize this store with {@value TimestampedCell#TIMESTAMPED_CELL_INIT}
	 */
	public SingleTimestampedCellStore() {}

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
		this.wl.lock();
		try
		{
			this.single_ts_cell = ts_cell;
		}
		finally
		{
			this.wl.unlock();
		}
	}

	/**
	 * ignore the {@link Timestamp} parameter;
	 * just get the latest one
	 */
	@Override
	public ITimestampedCell get(Timestamp ts)
	{
		return this.get();
	}

	@Override
	public ITimestampedCell get()
	{
		this.rl.lock();
		try
		{
			return this.single_ts_cell;
		}
		finally
		{
			this.rl.unlock();
		}
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.single_ts_cell)
				.toString();
	}

	@Override
	public void startGCDaemon()
	{
		throw new UnsupportedOperationException("GC is not supported for SingleTimestampedStore.");
	}
}
