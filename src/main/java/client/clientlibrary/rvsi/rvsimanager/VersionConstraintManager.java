package client.clientlibrary.rvsi.rvsimanager;

import java.io.Serializable;
import java.util.List;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;

/**
 * This {@link VersionConstraintManager} maintains 
 * a list of {@link AbstractVersionConstraint}, and
 * wraps their individual check() procedures in a single method. 
 *  
 * @author hengxin
 * @date Created on 11-17-2015
 */
public final class VersionConstraintManager implements Serializable {
	private static final long serialVersionUID = -4878469768601673830L;

	private List<AbstractVersionConstraint> vc_list;
	
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
		return this.vc_list.stream().allMatch(vc -> vc.check());
	}
}
