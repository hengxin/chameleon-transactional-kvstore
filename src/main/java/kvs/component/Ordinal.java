package kvs.component;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import net.jcip.annotations.ThreadSafe;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
public final class Ordinal implements Serializable {
	private static final long serialVersionUID = -8037347322531588752L;

	@NotNull
    private final AtomicLong ord;
	
	public Ordinal(long ord) { this.ord = new AtomicLong(ord); }

    /**
     * @return a <it>new</it> initial ordinal with value of 0L
     */
	public static Ordinal ORDINAL_INIT() { return new Ordinal(0L); }

	/**
	 * Atomically increments by one the current ordinal.
	 * @return 	the <i>new</i> updated ordinal.
	 */
	@NotNull
    public Ordinal incrementAndGet() { return new Ordinal(ord.incrementAndGet()); }

	public long getOrd() { return ord.get(); }
	
	/**
	 * Needed for possible equal-testing methods.
	 * For example, CollectionUtils#isEqualCollection()
	 * of org.apache.commons.collections4 uses {@link Map} internally.
	 */
	@Override
	public int hashCode() { return Objects.hashCode(ord.get()); }
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
        if (o == null || !(this.getClass() == o.getClass()))
            return false;

        Ordinal that = (Ordinal) o;
		return this.getOrd() == that.getOrd();
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("ord", ord)
				.toString();
	}

}
