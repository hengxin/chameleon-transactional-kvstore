package client.clientlibrary.rvsi.versionconstraints;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import kvs.component.Timestamp;
import kvs.compound.CompoundKey;

/**
 * @author hengxin
 * @date Created on 11-16-2015
 * 
 * <p> It consists of the basic elements of an {@link AbstractVersionConstraint}:
 * the key (of {@link CompoundKey}), one relevant timestamps (of {@link Timestamp}),
 * and an integer bound.
 */
public class VersionConstraintElement
{
	private CompoundKey vc_ck;
	private Timestamp vc_ts;
	private long vc_bound;
	
	public VersionConstraintElement(CompoundKey ck, Timestamp ts, long bound)
	{
		this.vc_ck = ck;
		this.vc_ts = ts;
		this.vc_bound = bound;
	}

	public CompoundKey getVcCk()
	{
		return vc_ck;
	}

	public Timestamp getVcTs()
	{
		return vc_ts;
	}

	public long getVcBound()
	{
		return vc_bound;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.vc_ck, this.vc_ts, this.vc_bound);
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
		
		VersionConstraintElement that = (VersionConstraintElement) o;
		return Objects.equal(this.vc_ck, that.vc_ck) && Objects.equal(this.vc_ts, that.vc_ts) && Objects.equal(this.vc_bound, that.vc_bound);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.vc_ck)
				.addValue(this.vc_ts)
				.add("bound", this.vc_bound)
				.toString();
	}
}
