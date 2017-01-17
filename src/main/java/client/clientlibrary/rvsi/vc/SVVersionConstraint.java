package client.clientlibrary.rvsi.vc;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.rvsi.rvsispec.SVSpecification;
import client.clientlibrary.rvsi.vc.vcresult.SVCheckedResult;
import client.clientlibrary.rvsi.vc.vcresult.VCCheckedResult;
import client.clientlibrary.transaction.QueryResults;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Snapshot-view version constraint generated according to {@link FVSpecification}
 * and {@link QueryResults}.
 * 
 * @see {@link SVSpecification}
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public final class SVVersionConstraint extends AbstractVersionConstraint {
    private static final long serialVersionUID = 6110803493107237501L;
    private static final Logger LOGGER = LoggerFactory.getLogger(SVVersionConstraint.class);

    public SVVersionConstraint(List<VCEntry> vcEntries) { super(vcEntries); }

    /**
     * @param table  check against this {@code table}
     * @param vce  {@link VCEntry} to be checked
     * @return O_x(T_l.cts) - ord(x_j) <= k3; see the paper.
     */
    @NotNull
    @Override
    public VCCheckedResult check(@NotNull AbstractTable table, @NotNull VCEntry vce) {
        ITimestampedCell tsCell = table.getTimestampedCell(vce.getVceCk(), vce.getVceTs());

        long ord = tsCell.getOrdinal().getOrd();
        boolean svChecked = (ord - vce.getVceOrd().getOrd() <= vce.getVceBound());

        LOGGER.debug("Checking SVVersionConstraint [{} vs. {}]: [{}] - [{}] <= [{}] with result [{}].",
                tsCell, vce, ord, vce.getVceOrd().getOrd(), vce.getVceBound(), svChecked);

        return new SVCheckedResult(svChecked);
    }

    @Override
    public Map<Integer, AbstractVersionConstraint> partition(@NotNull IPartitioner partitioner, int numberOfBuckets) {
        return vcEntries.stream()
                .collect(groupingBy(vce -> partitioner.locateSiteIndexFor(vce.getVceCk(), numberOfBuckets),
                        collectingAndThen(toList(), SVVersionConstraint::new)));
    }

}
