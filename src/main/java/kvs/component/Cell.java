package kvs.component;

import java.io.Serializable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import kvs.table.AbstractTable;
import net.jcip.annotations.Immutable;

/**
 * The actual values stored in the {@link AbstractTable}. 
 * @author hengxin
 * @date 10-27-2015
 */
@Immutable
public class Cell implements Serializable
{
	private static final long serialVersionUID = 4358362337464864235L;

	/**
	 * Initial value: "NULL"
	 */
	public final static Cell CELL_INIT = new Cell("NULL");
	
	public final String data;
	
	public Cell(String data)
	{
		this.data = data;
	}

	public String getData()
	{
		return this.data;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.data);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (o.getClass() == this.getClass()))
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