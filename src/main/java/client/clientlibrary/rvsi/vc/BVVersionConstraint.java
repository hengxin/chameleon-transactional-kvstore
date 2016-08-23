package client.clientlibrary.rvsi.vc;

import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.transaction.QueryResults;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Backward-view version constraint generated according to {@link BVSpecification}
 * and {@link QueryResults}.
 * 
 * @see {@link BVSpecification}
 * 
 * @author hengxin
 * @date Created on 11-16-2015 
 */
public final class BVVersionConstraint extends AbstractVersionConstraint {
    private static final long serialVersionUID = -4535031435746279370L;

    public BVVersionConstraint(List<VCEntry> vcEntries) { super(vcEntries); }

	@Override
	public boolean check(AbstractTable table) {
        return vcEntries.stream()
                .map(vce -> {
                    ITimestampedCell tsCell = table.getTimestampedCell(vce.getVceCk(), vce.getVceTs());
                    long ord = tsCell.getOrdinal().getOrd();
                    return (ord - vce.getVceOrd().getOrd() <= vce.getVceBound());
                })
                .allMatch(Boolean::booleanValue);
	}

    @Override
    public Map<Integer, AbstractVersionConstraint> partition(IPartitioner partitioner, int buckets) {
        return vcEntries.stream()
                .collect(groupingBy(vce -> partitioner.locateSiteIndexFor(vce.getVceCk(), buckets),
                        collectingAndThen(toList(), BVVersionConstraint::new)));
    }

}
