package client.clientlibrary.rvsi.vc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.io.Serializable;

import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;

/**
 * A {@link VCEntry} represents an entry of an {@link AbstractVersionConstraint}.
 * It consists of four parts:
 * <p><ul>
 * <li> {@link #ck}: the {@link CompoundKey} under constraint
 * <li> {@link #ord}: the <em>ordinal number</em> of an {@link ITimestampedCell}
 *  we are interested in; it should corresponds to the {@link #ck}.
 * <li> {@link #ts}: a {@link Timestamp} for {@link #ord} to be checked against
 * <li> {@link #bound}: the staleness bound allowed for {@link #ord}
 * </ul><p>
 * 
 * @author hengxin
 * @date Created on 11-28-2015
 */
public final class VCEntry implements Serializable {
    private static final long serialVersionUID = 6028572097957813169L;

    private final CompoundKey ck;
	private final Ordinal ord;
	private final Timestamp ts;
	private final int bound;
	
	public VCEntry(final CompoundKey ck, final Ordinal ord, final Timestamp ts, final int bound) {
		this.ck = ck;
		this.ord = ord;
		this.ts = ts;
		this.bound = bound;
	}

	public CompoundKey getVceCk() { return ck; }
	public Ordinal getVceOrd() { return ord; }
	public Timestamp getVceTs() { return ts; }
	public long getVceBound() { return bound; }
	
	@Override
	public int hashCode() { return Objects.hashCode(ck, ord, ts, bound); }
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (this.getClass() == o.getClass()))
			return false;
		
		VCEntry that = (VCEntry) o;
		return Objects.equal(this.ck, that.ck) && Objects.equal(this.ord, that.ord)
				&& Objects.equal(this.ts, that.ts) && Objects.equal(this.bound, that.bound);
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(ck)
				.addValue(ord)
				.addValue(ts)
				.add("bound", bound)
				.toString();
	}

}
