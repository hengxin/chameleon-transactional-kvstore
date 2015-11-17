package kvs.component;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author hengxin
 * @date Created on 11-16-2015
 * 
 * <p> We denote the position of some version of a data item <code>x</code> in its 
 * total version order by <code>x.ord</code>, called its ordinal number.
 */
public final class Ordinal
{
	public static Ordinal ORDINAL_INIT = new Ordinal();
	
	private long ord = 0L;	// TODO Or AtomicLong???
	
	public void increment()
	{
		this.ord++;
	}
	
	public long getOrd()
	{
		return ord;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.ord);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (o instanceof Ordinal))
			return false;
		
		Ordinal that = (Ordinal) o;
		return Objects.equal(this.ord, that.ord);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("ord", this.ord)
				.toString();
	}
}
