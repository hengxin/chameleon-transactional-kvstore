package kvs.table;

/**
 * @author hengxin
 * @date Created: 10-27-2015
 * 
 * Column keys of the {@link Table}.
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
	public int compareTo(Column col)
	{
		return this.column_key.compareTo(col.column_key);
	}
}
