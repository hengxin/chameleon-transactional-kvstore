/**
 * 
 */
package kvs.compound;

import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;

/**
 * @author hengxin
 * @date Created: 10-27-2015
 * 
 * Compound key = {@link Row} key + {@link Column} key, to uniquely identify a {@link Cell} value
 */
public class CompoundKey
{
	private final Row row;
	private final Column col;

	public CompoundKey(Row r, Column c)
	{
		this.row = r;
		this.col = c;
	}

	public Row getRow()
	{
		return this.row;
	}

	public Column getCol()
	{
		return this.col;
	}

}
