package client.clientlibrary.rvsi.vc;

import java.util.List;

/**
 * Forward-view version constraint generated according to {@link FVSpecification}
 * and {@link QueryResults}.
 * 
 * @see {@link FVSpecification}
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public class FVVersionConstraint extends AbstractVersionConstraint
{

	public FVVersionConstraint(List<VCEntry> vc_entry_list)
	{
		super(vc_entry_list);
	}

	@Override
	public boolean check()
	{
		return false;
	}

}
