package kvs.compound;

import java.util.Comparator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;

/**
 * A {@link KVItem} is a key-value pair of 
 * {@link CompoundKey} and {@link ITimestampedCell}.
 * 
 * @author hengxin
 * @date Created on 11-29-2015
 */
public final class KVItem
{
	public final static Comparator<KVItem> TIMESTAMP_COMPARATOR = new TimestampComparator();
	
	private final CompoundKey ck;
	private final ITimestampedCell ts_cell;
	
	public KVItem(final Row r, final Column c, final Cell cell)
	{
		this.ck = new CompoundKey(r, c);
		this.ts_cell = new TimestampedCell(cell);
	}
	
	public KVItem(final CompoundKey ck, final Cell cell)
	{
		this.ck = ck;
		this.ts_cell = new TimestampedCell(cell);
	}
	
	public KVItem(final CompoundKey ck, ITimestampedCell ts_cell)
	{
		this.ck = ck;
		this.ts_cell = ts_cell;
	}
	
	public CompoundKey getCK()
	{
		return ck;
	}
	
	public ITimestampedCell getTsCell()
	{
		return ts_cell;
	}
	
	/**
	 * Compare {@link KVItem} by their #ts_cell fields, which is in turn 
	 * compared by {@link Timestamp}.
	 * <p>
	 * <b>Note:</b> This ordering is not consistent with {@link #hashCode()}
	 * and {@link #equals(Object)}.
	 * 
	 * @author hengxin
	 * @date Created on Dec 30, 2015
	 */
	private static class TimestampComparator implements Comparator<KVItem>
	{
		@Override
		public int compare(KVItem left, KVItem right)
		{
			return ComparisonChain.start().compare(left.ts_cell, right.ts_cell).result();
		}
	}
	
	/**
	 * Only hashCode {@link #ck}.
	 */
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.ck);
	}
	
	/**
	 * Only check {@link #ck}.
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
		return Objects.equal(this.ck, that.ck);
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
