package kvs.table;

import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * The actual values stored in the {@link Table}. 
 */
public class Cell implements Serializable
{
	private static final long serialVersionUID = 4358362337464864235L;

	public static Cell CELL_INIT = new Cell("NULL");
	
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
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.data);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		
		if(! (o instanceof Cell))
			return false;
		
		Cell c = (Cell) o;
		return Objects.equal(this.data, c.data);
	}
}
