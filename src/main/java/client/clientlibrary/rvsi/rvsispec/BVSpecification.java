/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Triple;

import client.clientlibrary.rvsi.versionconstraints.AbstractVersionConstraint;
import client.clientlibrary.rvsi.versionconstraints.VersionConstraintElement;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Timestamp;
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
	public AbstractVersionConstraint generateVersionConstraint(Timestamp sts, QueryResults query_results)
	{
//		AbstractVersionConstraint.extractVersionConstraintElements(this, query_results).stream()
//			.<VersionConstraintElement>map(vc_triple -> new VersionConstraintElement(vc_triple.getLeft(), ts, vc_triple.getMiddle().getOrdinal().getOrd() + vc_triple.getRight()))
//			.collect(Collectors.toList());
		// TODO generate vc
		return null;
	}

}
