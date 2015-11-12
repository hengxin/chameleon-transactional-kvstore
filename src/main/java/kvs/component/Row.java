/**
 * 
 */
package kvs.component;

import java.io.Serializable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import kvs.table.AbstractTable;

/**
 * @author hengxin
 * @date Created: 10-27-2015
 * 
 * Row keys of the {@link AbstractTable}.
 */
public class Row implements Comparable<Row>, Serializable
{
	private static final long serialVersionUID = -971488511398300319L;

	private final String row_key;
	
	public Row(String key)
	{
		this.row_key = key;
	}
	
	public String getRowKey()
	{
		return this.row_key;
	}

	@Override
	public int compareTo(Row that)
	{
		return ComparisonChain.start().compare(this.row_key, that.row_key).result();
	}
	
	@Override
	public int hashCode() 
	{
		return Objects.hashCode(this.row_key);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		if(! (o instanceof Row))
			return false;
		
		Row that = (Row) o;
		return Objects.equal(this.row_key, that.row_key);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this).add("row_key", this.row_key).toString();
	}
}
