package client.clientlibrary.rvsi.vc;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.rvsi.vc.vcresult.BVCheckedResult;
import client.clientlibrary.rvsi.vc.vcresult.VCCheckedResult;
import client.clientlibrary.transaction.QueryResults;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Backward-view version constraint generated according to
 * {@link BVSpecification} and {@link QueryResults}.
 * 
 * @author hengxin
 * @date Created on 11-16-2015 
 */
public final class BVVersionConstraint extends AbstractVersionConstraint {
    private static final long serialVersionUID = -4535031435746279370L;
    private static final Logger LOGGER = LoggerFactory.getLogger(BVVersionConstraint.class);

    public BVVersionConstraint(List<VCEntry> vcEntries) { super(vcEntries); }

    /**
     * @param table  check against this {@code table}
     * @param vce  {@link VCEntry} to be checked
     * @return O_x(T_i.sts) - ord(x_j) <= k1; see the paper.
     */
    @NotNull
    @Override
    public VCCheckedResult check(@NotNull AbstractTable table, @NotNull VCEntry vce) {
        ITimestampedCell tsCell = table.getTimestampedCell(vce.getVceCk(), vce.getVceTs());
        LOGGER.debug("TsCell to check is : [{}].", tsCell);

        long ord = tsCell.getOrdinal().getOrd();
        boolean bvChecked = (ord - vce.getVceOrd().getOrd() < vce.getVceBound());

        LOGGER.debug("Checking BVVersionConstraint [{} vs. {}]: [{}] - [{}] < [{}] with result [{}].",
                tsCell, vce, ord, vce.getVceOrd().getOrd(), vce.getVceBound(), bvChecked);

        return new BVCheckedResult(bvChecked);
    }

    @Override
    public Map<Integer, AbstractVersionConstraint> partition(@NotNull IPartitioner partitioner, int numberOfBuckets) {
        return vcEntries.stream()
                .collect(groupingBy(vce -> partitioner.locateSiteIndexFor(vce.getVceCk(), numberOfBuckets),
                        collectingAndThen(toList(), BVVersionConstraint::new)));
    }

}
