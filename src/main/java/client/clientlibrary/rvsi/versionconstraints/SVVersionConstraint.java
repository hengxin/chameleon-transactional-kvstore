package client.clientlibrary.rvsi.versionconstraints;

import java.util.List;

/**
 * @author hengxin
 * @date Created on 11-16-2015
 * 
 * <p> Snapshot-view version constraint generated according to {@link FVSpecification}
 * and {@link QueryResults}.
 * 
 * @see {@link FVSpecification}
 */
public class SVVersionConstraint extends AbstractVersionConstraint
{

	public SVVersionConstraint(List<VersionConstraintElement> vc_element_list)
	{
		super(vc_element_list);
	}

	@Override
	public boolean check()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
