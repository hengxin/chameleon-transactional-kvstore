package kvs.compound;

import java.util.Comparator;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

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
public final class KVItem {

	private final CompoundKey ck;
	private final ITimestampedCell ts_cell;
	
	public KVItem(final Row r, final Column c, final Cell cell) {
		this(new CompoundKey(r, c), new TimestampedCell(cell));
	}
	
	public KVItem(final CompoundKey ck, final Cell cell) {
		this(ck, new TimestampedCell(cell));
	}
	
	public KVItem(final CompoundKey ck, ITimestampedCell ts_cell) {
		this.ck = ck;
		this.ts_cell = ts_cell;
	}
	
	/**
	 * {@link Comparator} for {@link KVItem} by their {@link #ts_cell}, which in turn compared by {@link Timestamp}.
	 * <p>
	 * <b>Note:</b> This ordering is not consistent with {@link #hashCode()} and {@link #equals(Object)}.
	 */
	public static final Comparator<KVItem> COMPARATOR_BY_TIMESTAMP = Comparator.comparing(KVItem::getTsCell);
	
	public CompoundKey getCK() {
		return ck;
	}
	
	public ITimestampedCell getTsCell() {
		return ts_cell;
	}
	
	/**
	 * Only hashCode {@link #ck}.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.ck);
	}
	
	/**
	 * Only check {@link #ck}.
	 */
	@Override
	public boolean equals(Object o) {
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
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(this.ck)
				.addValue(this.ts_cell)
				.toString();
	}
}
