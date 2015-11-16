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
}
