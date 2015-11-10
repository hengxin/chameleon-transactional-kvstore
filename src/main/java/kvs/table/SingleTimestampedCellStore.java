package kvs.table;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author hengxin
 * @data Created on 11-10-2015
 * 
 * Implements the interface {@link ITimestampedCellStore}.
 * It maintains only a single {@link ITimestampedCell}.
 */
public class SingleTimestampedCellStore implements ITimestampedCellStore
{

//	private Pair<Timestamp, Cell> ts_cell = new ImmutablePair<>(Timestamp.TIMESTAMP_INIT, Cell.CELL_INIT);
	private ITimestampedCell single_ts_cell = new TimestampedCell();
	
	private final ReadWriteLock rw_lock = new ReentrantReadWriteLock(true);
	private final Lock rl = rw_lock.readLock();
	private final Lock wl = rw_lock.writeLock();

	@Override
	public void update(ITimestampedCell ts_cell)
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

}
