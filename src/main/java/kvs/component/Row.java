/**
 * 
 */
package kvs.component;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import kvs.table.AbstractTable;

/**
 * @author hengxin
 * @date Created: 10-27-2015
 * 
 * Row keys of the {@link AbstractTable}.
 */
public class Row implements Comparable<Row>
{
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
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		if(! (o instanceof Row))
			return false;
		
		Row that = (Row) o;
		return Objects.equal(this.row_key, that.row_key);
	}
}
