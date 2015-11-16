/**
 * 
 */
package kvs.compound;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import kvs.component.Column;
import kvs.component.Row;
import kvs.table.ITimestampedCellStore;

/**
 * @author hengxin
 * @date Created: 10-27-2015
 * 
 * Compound key = {@link Row} key + {@link Column} key, to uniquely identify an {@link ITimestampedCellStore} 
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
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.row, this.col);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (o instanceof CompoundKey))
			return false;
		
		CompoundKey that = (CompoundKey) o;
		return Objects.equal(this.row, that.row) && Objects.equal(this.col, that.col);
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.row).addValue(this.col)
				.toString();
	}
	
	public static void main(String[] args)
	{
		CompoundKey ck = new CompoundKey(new Row("Row"), new Column("Col"));
		// Output: CompoundKey{Row{row_key=Row}, Column{col_key=Col}}
		System.out.println(ck);
	}
}
