package client.clientlibrary.rvsi.versionconstraints;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;

/**
 * A {@link VCEntry} represents an entry of an {@link AbstractVersionConstraint}.
 * It consists of four parts:
 * <p><ul>
 * <li> {@link #vce_ck}: the {@link CompoundKey} under constraint
 * <li> {@link #vce_ord}: the <em>ordinal number</em> of an {@link ITimestampedCell} 
 *  we are interested in; it should corresponds to the {@link #vce_ck}.
 * <li> {@link #vce_ts}: a {@link Timestamp} for {@link #vce_ord} to be checked against
 * <li> {@link #vce_bound}: the staleness bound allowed for {@link #vce_ord}
 * </ul><p>
 * 
 * @author hengxin
 * @date Created on 11-28-2015
 */
public class VCEntry
{
	private final CompoundKey vce_ck;
	private final Ordinal vce_ord;
	private final Timestamp vce_ts;
	private final long vce_bound;
	
	public VCEntry(final CompoundKey ck, final Ordinal ord, final Timestamp ts, final long bound)
	{
		this.vce_ck = ck;
		this.vce_ord = ord;
		this.vce_ts = ts;
		this.vce_bound = bound;
	}

	public CompoundKey getVceCk()
	{
		return vce_ck;
	}

	public Ordinal getVceOrd()
	{
		return vce_ord;
	}

	public Timestamp getVceTs()
	{
		return vce_ts;
	}

	public long getVceBound()
	{
		return vce_bound;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(vce_ck, vce_ord, vce_ts, vce_bound);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (this.getClass() == o.getClass()))
			return false;
		
		VCEntry that = (VCEntry) o;
		return Objects.equal(this.vce_ck, that.vce_ck) && Objects.equal(this.vce_ord, that.vce_ord)
				&& Objects.equal(this.vce_ts, that.vce_ts) && Objects.equal(this.vce_bound, that.vce_bound);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(vce_ck)
				.addValue(vce_ord)
				.addValue(vce_ts)
				.add("bound", vce_bound)
				.toString();
	}
}
