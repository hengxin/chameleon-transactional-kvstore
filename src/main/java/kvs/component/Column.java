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
 * Column keys of the {@link AbstractTable}.
 */
public class Column implements Comparable<Column>, Serializable
{
	private static final long serialVersionUID = -1528275933592207808L;

	private final String col_key;
	
	public Column(String key)
	{
		this.col_key = key;
	}

	public String getColumnKey()
	{
		return this.col_key;
	}

	@Override
	public int compareTo(Column that)
	{
		return ComparisonChain.start().compare(this.col_key, that.col_key).result();
	}

	@Override
	public int hashCode() 
	{
		return Objects.hashCode(this.col_key);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		if(! (o instanceof Column))
			return false;
		
		Column that = (Column) o;
		return Objects.equal(this.col_key, that.col_key);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this).add("col_key", this.col_key).toString();
	}
}
