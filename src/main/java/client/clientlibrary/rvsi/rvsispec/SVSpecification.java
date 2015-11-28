/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date 10-27-2015
 */
public class SVSpecification extends AbstractRVSISpecification
{

	@Override
	public void extractVCEntryRawInfo(QueryResults query_results)
	{
	}
	
	@Override
	public AbstractVersionConstraint generateVersionConstraint(Timestamp sts)
	{
		return null;
	}

}
