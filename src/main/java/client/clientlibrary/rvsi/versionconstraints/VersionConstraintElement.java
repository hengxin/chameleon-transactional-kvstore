package client.clientlibrary.rvsi.versionconstraints;

import kvs.component.Timestamp;
import kvs.compound.CompoundKey;

/**
 * @author hengxin
 * @date Created on 11-16-2015
 * 
 * <p> It consists of the basic elements of an {@link AbstractVersionConstraint}:
 * the key (of {@link CompoundKey}), one relevant timestamps (of {@link Timestamp}),
 * and an Integer bound.
 */
public class VersionConstraintElement
{
	private CompoundKey vc_ck;
	private Timestamp vc_ts;
	private int vc_bound;
	
	public VersionConstraintElement(CompoundKey ck, Timestamp ts, int bound)
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

	public int getVcBound()
	{
		return vc_bound;
	}
	
	
}
