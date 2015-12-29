package kvs.compound;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import kvs.component.Timestamp;

/**
 * A {@link KVItem} is a key-value pair of 
 * {@link CompoundKey} and {@link ITimestampedCell}.
 * 
 * @author hengxin
 * @date Created on 11-29-2015
 */
public final class KVItem implements Comparable<KVItem>
{
	private final CompoundKey ck;
	private final ITimestampedCell ts_cell;
	
	public KVItem(final CompoundKey ck, ITimestampedCell ts_cell)
	{
		this.ck = ck;
		this.ts_cell = ts_cell;
	}
	
	public CompoundKey getCk()
	{
		return ck;
	}
	
	public ITimestampedCell getTscell()
	{
		return ts_cell;
	}

	/**
	 * Compared by {@link #ts_cell}, which is (see {@link TimestampedCell}) 
	 * in turn sorted by {@link Timestamp}.
	 */
	@Override
	public int compareTo(KVItem that)
	{
		return ComparisonChain.start().compare(this.ts_cell, that.ts_cell).result();
	}
	
	/**
	 * Only hashCode {@link #ts_cell}.
	 * Consistent with {@link #compareTo(KVItem)}.
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.ts_cell);
	}
	
	/**
	 * Only check {@link #ts_cell}.
	 * Consistent with {@link #compareTo(KVItem)}.
	 */
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (this.getClass() == o.getClass()))
			return false;
		
		KVItem that = (KVItem) o;
		return Objects.equal(this.ts_cell, that.ts_cell);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.ck)
				.addValue(this.ts_cell)
				.toString();
	}
}
