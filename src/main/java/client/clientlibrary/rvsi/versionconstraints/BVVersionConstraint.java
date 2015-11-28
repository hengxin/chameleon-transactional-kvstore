package client.clientlibrary.rvsi.versionconstraints;

import java.util.List;

/**
 * Backward-view version constraint generated according to {@link BVSpecification}
 * and {@link QueryResults}.
 * 
 * @see {@link BVSpecification}
 * 
 * @author hengxin
 * @date Created on 11-16-2015 
 */
public class BVVersionConstraint extends AbstractVersionConstraint
{

	public BVVersionConstraint(List<VCEntry> vc_entry_list)
	{
		super(vc_entry_list);
	}

	@Override
	public boolean check()
	{
		return false;
	}

}
