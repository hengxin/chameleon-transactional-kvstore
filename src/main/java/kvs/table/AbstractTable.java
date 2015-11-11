/**
 * 
 */
package kvs.table;

import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;

import org.junit.Assert;

import com.google.common.collect.TreeBasedTable;

import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

/**
 * @author hengxin
 * @date Created: 10-24-2015
 * 
 * <b>WARNING:</b> Making it thread-safe while keeping it efficient. 
 */
public abstract class AbstractTable
{
	private TreeBasedTable<Row, Column, ITimestampedCellStore> table = TreeBasedTable.create();
	
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
	 * get the <i>latest</i> preceding {@link ITimestampedCell} with {@link Timestamp} smaller than @param ts, 
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
		return Optional.ofNullable(table.get(row, col));
	}
	
	public void put(Row row, Map<Column, ITimestampedCell> col_data_map)
	{
		//TODO: 
	}
	
	
	/**
	 * adding data (row, column, timestamped-cell)
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @param tc {@link ITimestampedCell}
	 * 
	 * Template design pattern 
	 */
	public void put(Row row, Column col, ITimestampedCell tc)
	{
		Optional<ITimestampedCellStore> ts_cell_store = this.getTimestampedCellStore(row, col);
		
		if(ts_cell_store.isPresent()) 
			ts_cell_store.get().put(tc);
		else 
			this.put(row, col, this.initStore(tc));
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
		this.table.put(row, col, ts_cell_store);
	}
}
