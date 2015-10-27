/**
 * 
 */
package kvs.table;

/**
 * @author hengxin
 * @date Created: 10-27-2015
 * 
 * Row keys of the {@link Table}.
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
	public int compareTo(Row r)
	{
		return this.row_key.compareTo(r.row_key);
	}
}
