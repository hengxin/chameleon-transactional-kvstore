/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import client.clientlibrary.rvsi.versionconstraints.AbstractVersionConstraint;
import client.clientlibrary.transaction.QueryResults;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * k1-bv (backward view) specification
 */
public class BVSpecification extends AbstractRVSISpecification
{

	@Override
	public AbstractVersionConstraint generateVersionConstraint(QueryResults query_results)
	{
		// TODO to complete this!
		super.rvsi_spec_map.entrySet().forEach(
				rvsi_spec_entry -> 
				{query_results.getQueryResults().entrySet().forEach(
						query_result_entry ->
						{});
				});
		return null;
	}

}
