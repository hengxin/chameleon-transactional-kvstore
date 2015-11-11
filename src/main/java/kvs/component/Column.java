package kvs.component;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import kvs.table.AbstractTable;

/**
 * @author hengxin
 * @date Created: 10-27-2015
 * 
 * Column keys of the {@link AbstractTable}.
 */
public class Column implements Comparable<Column>
{
	private final String column_key;
	
	public Column(String key)
	{
		this.column_key = key;
	}

	public String getColumnKey()
	{
		return this.column_key;
	}

	@Override
	public int compareTo(Column that)
	{
		return ComparisonChain.start().compare(this.column_key, that.column_key).result();
	}

	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		if(! (o instanceof Column))
			return false;
		
		Column that = (Column) o;
		return Objects.equal(this.column_key, that.column_key);
	}
}
