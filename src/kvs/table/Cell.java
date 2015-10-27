package kvs.table;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * The actual values stored in the {@link Table}. 
 */
public class Cell
{
	public String data;
	
	public Cell(String data)
	{
		this.data = data;
	}

	public String getData()
	{
		return this.data;
	}

	public void setData(String data)
	{
		this.data = data;
	}
}
