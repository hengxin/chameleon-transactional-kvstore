package client.clientlibrary.rvsi.versionconstraints;

import java.util.List;

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
	private List<VersionConstraintElement> vc_element_list;
	
	public abstract boolean check();
}
