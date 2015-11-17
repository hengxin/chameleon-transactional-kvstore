/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import java.util.List;

import org.apache.commons.lang3.tuple.Triple;

import client.clientlibrary.rvsi.versionconstraints.AbstractVersionConstraint;
import client.clientlibrary.transaction.QueryResults;
import kvs.compound.CompoundKey;
import kvs.compound.TimestampedCell;

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
		List<Triple<CompoundKey, TimestampedCell, Integer>> vc_triple_list = AbstractVersionConstraint.extractVersionConstraintElements(this, query_results);
		// TODO generate vc
		return null;
	}

}
