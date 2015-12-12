package kvs.component;

import java.util.concurrent.atomic.AtomicLong;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import net.jcip.annotations.ThreadSafe;

/**
 * We denote the position of some version of a data item <code>x</code> in its 
 * total version order by <code>x.ord</code>, called its ordinal number.
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 * 
 * @implNote
 * 	{@link Ordinal} has been implemented as a wrapper of an {@link AtomicLong}.
 */
@ThreadSafe
public final class Ordinal
{
	public final static Ordinal ORDINAL_INIT = new Ordinal(0L);
	
	private final AtomicLong ord;	
	
	public Ordinal(long ord)
	{
		this.ord = new AtomicLong(ord);
	}
	
	/**
	 * Atomically increments by one the current ordinal.
	 * @return
	 * 	the <i>new</i> updated ordinal.
	 */
	public Ordinal incrementAndGet()
	{
		return new Ordinal(this.ord.incrementAndGet());
	}
	
	/**
	 * @return
	 * 	the {@link #ord} value
	 */
	public long getOrd()
	{
		return this.ord.get();
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
