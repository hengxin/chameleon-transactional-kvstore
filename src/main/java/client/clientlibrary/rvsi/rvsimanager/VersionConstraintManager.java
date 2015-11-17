package client.clientlibrary.rvsi.rvsimanager;

import java.util.ArrayList;
import java.util.List;

import client.clientlibrary.rvsi.versionconstraints.AbstractVersionConstraint;

/**
 * @author hengxin
 * @date Created on 11-17-2015
 * 
 * <p> Manager of version constraints related tasks:
 * maintaining a list of {@link AbstractVersionConstraint}
 * and triggering their checking procedures.
 */
public class VersionConstraintManager
{
	private List<AbstractVersionConstraint> vc_list = new ArrayList<>();
	
	public VersionConstraintManager()
	{
		
	}
	
	public VersionConstraintManager(List<AbstractVersionConstraint> vc_list)
	{
		this.vc_list = vc_list;
	}
	
	/**
	 * Check whether all the {@link AbstractVersionConstraint} can be satisfied.
	 * @return <code>true</code> if all can be satisfied; <code>false</code>, otherwise.
	 */
	public boolean check()
	{
		return this.vc_list.stream().map(AbstractVersionConstraint::check)
				.allMatch(check_result -> check_result == true);
	}
}
