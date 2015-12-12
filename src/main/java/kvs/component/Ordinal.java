package kvs.component;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.base.MoreObjects;

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
 *  Note that it does not implement its own {@link #hashCode()} and {@link #equals(Object)}
 *  methods either.
 *  See <a href = "http://stackoverflow.com/a/7568493/1833118">
 *  	Why are two AtomicIntegers never equal? @ StackOverflow</a>
 */
@ThreadSafe
public final class Ordinal implements Serializable
{
	private static final long serialVersionUID = -8037347322531588752L;

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
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("ord", this.ord)
				.toString();
	}
}
