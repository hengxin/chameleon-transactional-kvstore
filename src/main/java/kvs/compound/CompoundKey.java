/**
 * 
 */
package kvs.compound;

import java.io.Serializable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import kvs.component.Column;
import kvs.component.Row;
import kvs.table.ITimestampedCellStore;
import net.jcip.annotations.Immutable;

/**
 * {@link CompoundKey} = {@link Row} key + {@link Column} key, 
 * to uniquely identify an {@link ITimestampedCellStore}. 
 * 
 * @author hengxin
 * @date Created: 10-27-2015
 */
@Immutable
public class CompoundKey implements Serializable
{
	private static final long serialVersionUID = -4184998600697675989L;

	private final Row row;
	private final Column col;

	public CompoundKey(Row r, Column c)
	{
		this.row = r;
		this.col = c;
	}

	public CompoundKey(String r, String c)
	{
		this.row = new Row(r);
		this.col = new Column(c);
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
		if(! (this.getClass() == o.getClass()))
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
}
