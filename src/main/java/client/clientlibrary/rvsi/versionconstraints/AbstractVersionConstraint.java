package client.clientlibrary.rvsi.versionconstraints;

import java.sql.Timestamp;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;

/**
 * @author hengxin
 * @date Created on 11-16-2015
 * 
 * <p> Representing the version constraints generated 
 * according to the {@link AbstractRVSISpecification}.
 */
public abstract class AbstractVersionConstraint
{
	private Timestamp self_ts;
	private Timestamp other_ts;
	private int bound;
	
	
	public abstract boolean check();
}
