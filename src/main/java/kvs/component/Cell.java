package kvs.component;

import java.io.Serializable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import kvs.table.AbstractTable;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * The actual values stored in the {@link AbstractTable}. 
 */
public class Cell implements Serializable
{
	private static final long serialVersionUID = 4358362337464864235L;

	/**
	 * Initial value: "NULL"
	 */
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
		
		Cell that = (Cell) o;
		return Objects.equal(this.data, that.data);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this).add("data", this.data).toString();
	}
}
