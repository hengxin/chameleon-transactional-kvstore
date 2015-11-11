/**
 * 
 */
package kvs.table;

import java.util.Map;
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
public class Table
{
	TreeBasedTable<Row, Column, ITimestampedCellStore> table = TreeBasedTable.create();
	
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
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @return an {@link ITimestampedCell}
	 */
	public ITimestampedCell getTimestampedCell(Row row, Column col)
	{
		return this.getTimestampedCellStore(row, col).get();
	}
	
	/**
	 * get the <i>latest</i> preceding {@link ITimestampedCell} with {@link Timestamp} smaller than @param ts, 
	 * indexed by a {@link Row} key (@param row) and a {@link Column} key (@param col)
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @param ts {@link Timestamp} to compare
	 * @return an {@link ITimestampedCell}
	 */
	public ITimestampedCell getTimestampedCell(Row row, Column col, Timestamp ts)
	{
		return this.getTimestampedCellStore(row, col).get(ts);
	}
	
	/**
	 * get an {@link ITimestampedCellStore} indexed 
	 * by a {@link Row} key (@param row) and a {@link Column} key (@param col)
	 * 
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @return an {@link ITimestampedCellStore}
	 */
	private ITimestampedCellStore getTimestampedCellStore(Row row, Column col)
	{
		return table.get(row, col);
	}
	
	public void put(Row row, Map<Column, ITimestampedCell> col_data_map)
	{
		//TODO: 
	}
	
	
	/**
	 * put a triple (row, column, timestamped-cell)
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @param tc {@link ITimestampedCell}
	 */
	public void put(Row row, Column col, ITimestampedCell tc)
	{
	}
}
