/**
 * 
 */
package kvs.table;

import java.util.Map;
import java.util.Optional;
import java.util.SortedMap;

import com.google.common.collect.TreeBasedTable;

import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;

/**
 * @author hengxin
 * @date Created: 10-24-2015
 * 
 * <b>WARNING:</b> Making it thread-safe while keeping it efficient. 
 */
public abstract class Table
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
	 * <b>Note:</b> The result could be null, represented by <code>Optional.empty()</code>.
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @return an {@link Optional} wrapper of {@link ITimestampedCell}
	 */
	public Optional<ITimestampedCell> getTimestampedCell(Row row, Column col)
	{
		Optional<ITimestampedCellStore> ts_cell_store = this.getTimestampedCellStore(row, col);
		return ts_cell_store.isPresent() ? Optional.of(ts_cell_store.get().get()) : Optional.empty(); 
	}
	
	/**
	 * get the <i>latest</i> preceding {@link ITimestampedCell} with {@link Timestamp} smaller than @param ts, 
	 * indexed by a {@link Row} key (@param row) and a {@link Column} key (@param col).
	 * 
	 * <b>Note:</b> The result could be null, represented by <code>Optional.empty()</code>.
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @param ts {@link Timestamp} to compare
	 * @return an {@link Optional} wrapper of {@link ITimestampedCell}
	 */
	public Optional<ITimestampedCell> getTimestampedCell(Row row, Column col, Timestamp ts)
	{
		Optional<ITimestampedCellStore> ts_cell_store = this.getTimestampedCellStore(row, col);
		return ts_cell_store.isPresent() ? Optional.of(ts_cell_store.get().get(ts)) : Optional.empty(); 
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
	private Optional<ITimestampedCellStore> getTimestampedCellStore(Row row, Column col)
	{
		return Optional.of(table.get(row, col));
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
	 */
	public abstract void put(Row row, Column col, ITimestampedCell tc);
//		ITimestampedCellStore ts_cell_store = this.getTimestampedCellStore(row, col).orElseGet(() -> new )
}
