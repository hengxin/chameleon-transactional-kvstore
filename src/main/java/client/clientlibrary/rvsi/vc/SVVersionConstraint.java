package client.clientlibrary.rvsi.vc;

import java.util.List;

/**
 * Snapshot-view version constraint generated according to {@link FVSpecification}
 * and {@link QueryResults}.
 * 
 * @see {@link FVSpecification}
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public class SVVersionConstraint extends AbstractVersionConstraint
{

	public SVVersionConstraint(List<VCEntry> vc_entry_list)
	{
		super(vc_entry_list);
	}

	@Override
	public boolean check()
	{
		// TODO Auto-generated method stub
		return false;
	}

}
