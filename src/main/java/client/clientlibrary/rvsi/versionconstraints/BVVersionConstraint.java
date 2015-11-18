package client.clientlibrary.rvsi.versionconstraints;

import java.util.List;

/**
 * @author hengxin
 * @date Created on 11-16-2015 
 * 
 * <p> Backward-view version constraint generated according to {@link BVSpecification}
 * and {@link QueryResults}.
 * 
 * @see {@link BVSpecification}
 */
public class BVVersionConstraint extends AbstractVersionConstraint
{

	public BVVersionConstraint(List<VersionConstraintElement> vc_element_list)
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
