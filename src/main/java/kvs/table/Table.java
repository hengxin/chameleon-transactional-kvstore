/**
 * 
 */
package kvs.table;

import java.util.Map;

import com.google.common.collect.TreeBasedTable;

/**
 * @author hengxin
 * @date Created: 10-24-2015
 */
public class Table
{
	TreeBasedTable<Row, Column, Cell> table = TreeBasedTable.create();
	
	/**
	 * get a row with a specific row key
	 * @param row row key
	 * @return a row
	 */
	public Map<Column, Cell> getRow(Row row)
	{
		return table.row(row);
	}
	
	/**
	 * get a cell indexed by a @param row key and a @param col key
	 * @param row row key
	 * @param col column key
	 * @return a cell
	 */
	public Cell getCell(Row row, Column col)
	{
		return table.get(row, col);
	}
	
	public void put(Row row, Map<Column, Cell> col_data_map)
	{
		//TODO: 
	}
	
	
	/**
	 * put a triple (row, column, data)
	 * @param row row key
	 * @param col column key
	 * @param data actual data to store
	 */
	public void put(Row row, Column col, Cell data)
	{
		this.table.put(row, col, data);
	}
}
