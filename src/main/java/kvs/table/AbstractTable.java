/**
 * 
 */
package kvs.table;

import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Assert;

import com.google.common.collect.TreeBasedTable;

import client.clientlibrary.transaction.BufferedUpdates;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

/**
 * @author hengxin
 * @date Created: 10-24-2015
 * 
 * <p> In this implementation, the synchronization is achieved by {@link ReentrantReadWriteLock}.
 * <p> TODO using the built-in synchronization mechanism of {@link TreeBasedTable}.
 */
public abstract class AbstractTable
{
	private TreeBasedTable<Row, Column, ITimestampedCellStore> table = TreeBasedTable.create();
	
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock read_lock = this.lock.readLock();
	private final Lock write_lock = this.lock.writeLock();
	
	/**
	 * get a row with a specific row key
	 * @param row {@link Row} key
	 * @return a table row
	 * 
	 * FIXME not implemented yet
	 */
	public SortedMap<Column, ITimestampedCellStore> getRow(Row row)
	{
//		return table.row(row);
		return null;
	}
	
	/**
	 * get the <i>latest</i> {@link ITimestampedCell} indexed by a {@link Row} key (@param row)
	 * and a {@link Column} key (@param col)
	 * 
	 * <b>Note:</b> The result could be "NULL" (the initial value), 
	 * represented by {@value TimestampedCell#TIMESTAMPED_CELL_INIT}.
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @return an {@link Optional} wrapper of {@link ITimestampedCell}
	 */
	public ITimestampedCell getTimestampedCell(Row row, Column col)
	{
		Optional<ITimestampedCellStore> ts_cell_store = this.getTimestampedCellStore(row, col);
		return ts_cell_store.isPresent() ? ts_cell_store.get().get() : TimestampedCell.TIMESTAMPED_CELL_INIT; 
	}
	
	/**
	 * get the <em>latest</em> preceding {@link ITimestampedCell} with {@link Timestamp} smaller than @param ts, 
	 * indexed by a {@link Row} key (@param row) and a {@link Column} key (@param col).
	 * 
	 * <b>Note:</b> The result could be "NULL" (the initial value), 
	 * represented by {@value TimestampedCell#TIMESTAMPED_CELL_INIT}.
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @param ts {@link Timestamp} to compare
	 * @return an {@link Optional} wrapper of {@link ITimestampedCell}
	 */
	public ITimestampedCell getTimestampedCell(Row row, Column col, Timestamp ts)
	{
		Optional<ITimestampedCellStore> ts_cell_store = this.getTimestampedCellStore(row, col);
		return ts_cell_store.isPresent() ? ts_cell_store.get().get(ts) : TimestampedCell.TIMESTAMPED_CELL_INIT;
	}
	
	/**
	 * get an {@link ITimestampedCellStore} indexed 
	 * by a {@link Row} key (@param row) and a {@link Column} key (@param col)
	 * 
	 * <b>Note:</b> The result could be null, represented by <code>Optional.empty()</code>.
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @return an {@link Optional} wrapper of {@link ITimestampedCellStore}
	 */
	protected Optional<ITimestampedCellStore> getTimestampedCellStore(Row row, Column col)
	{
		this.read_lock.lock();
		try
		{
			return Optional.ofNullable(table.get(row, col));
		} finally
		{
			this.read_lock.unlock();
		}
	}
	
	public void put(Row row, Map<Column, ITimestampedCell> col_data_map)
	{
		//TODO: 
	}
	
	/**
	 * Apply all the {@link BufferedUpdates} with timestamp @param cts to this {@link #table}.
	 * @param buffered_updates {@link BufferedUpdates}
	 */
	public void apply(Timestamp cts, BufferedUpdates buffered_updates)
	{
		buffered_updates.getBufferedUpdateMap().entrySet()
			.forEach(
					update_entry -> { this.put(update_entry.getKey(), new TimestampedCell(cts, update_entry.getValue())); }
					);
	}
	
	/**
	 * Put data ({@link CompoundKey}, {@link ITimestampedCell}) into this {@link #table}.
	 */
	public void put(CompoundKey ck, ITimestampedCell tc)
	{
		this.put(ck.getRow(), ck.getCol(), tc);
	}
	
	/**
	 * Putting data (row, column, timestamped-cell) into this {@link #table}.
	 * <p> If the {@link ITimestampedCellStore} corresponding to (row, column) does not
	 * exist, then initialize one and put the timestamped-cell into it.
	 * The synchronization mechanism should prevent an {@link ITimestampedCellStore} 
	 * corresponding to the same (row, column) from being initialized twice.
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @param tc the {@link ITimestampedCell} to store
	 * 
	 * Template design pattern 
	 */
	public void put(Row row, Column col, ITimestampedCell tc)
	{
		this.write_lock.lock();
		try
		{
			Optional<ITimestampedCellStore> ts_cell_store = this.getTimestampedCellStore(row, col);
		
			if(ts_cell_store.isPresent()) 
				ts_cell_store.get().put(tc);
			else 
				this.put(row, col, this.initStore(tc)); 
		} finally
		{
			this.write_lock.unlock();
		}
	}
	
	public abstract ITimestampedCellStore initStore(ITimestampedCell tc);
	
	/**
	 * adding data (row, column, timestamped-cell-store)
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @param ts_cell_store {@link ITimestampedCellStore}; this parameter cannot be null.
	 */
	protected void put(Row row, Column col, ITimestampedCellStore ts_cell_store)
	{
		Assert.assertNotNull("The parameter ITimestampedCellStore cannot be null.", ts_cell_store);
		this.write_lock.lock();
		try
		{
			this.table.put(row, col, ts_cell_store);
		} finally
		{
			this.write_lock.unlock();
		}
	}
}
