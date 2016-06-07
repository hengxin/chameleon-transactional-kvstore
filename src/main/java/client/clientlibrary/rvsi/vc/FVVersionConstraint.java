package client.clientlibrary.rvsi.vc;

import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.transaction.QueryResults;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Forward-view version constraint generated according to {@link FVSpecification}
 * and {@link QueryResults}.
 * 
 * @see {@link FVSpecification}
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public final class FVVersionConstraint extends AbstractVersionConstraint {

	public FVVersionConstraint(List<VCEntry> vcEntries) {
		super(vcEntries);
	}

	@Override
	public boolean check() {
        // TODO Auto-generated method stub
		return false;
	}

    @Override
    public Map<Integer, AbstractVersionConstraint> partition(IPartitioner partitioner, int buckets) {
        return vcEntries.stream()
                .collect(groupingBy(vce -> partitioner.locateSiteIndexFor(vce.getVceCk(), buckets),
                        collectingAndThen(toList(), FVVersionConstraint::new)));
    }

}
