package client.clientlibrary.rvsi.vc;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.rvsi.vc.vcresult.FVCheckedResult;
import client.clientlibrary.rvsi.vc.vcresult.VCCheckedResult;
import client.clientlibrary.transaction.QueryResults;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * Forward-view version constraint generated according to
 * {@link FVSpecification} and {@link QueryResults}.
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public final class FVVersionConstraint extends AbstractVersionConstraint {
    private static final long serialVersionUID = 6243767991094395996L;
    private static final Logger LOGGER = LoggerFactory.getLogger(FVVersionConstraint.class);

    public FVVersionConstraint(List<VCEntry> vcEntries) { super(vcEntries); }

    /**
     * @param table  check against this {@code table}
     * @param vce  {@link VCEntry} to be checked
     * @return ord(x_j) - O_x(T_i.sts) < k2; see the paper.
     */
    @NotNull
    @Override
    public VCCheckedResult check(@NotNull AbstractTable table, @NotNull VCEntry vce) {
        ITimestampedCell tsCell = table.getTimestampedCell(vce.getVceCk(), vce.getVceTs());

        long ord = tsCell.getOrdinal().getOrd();
        boolean fvChecked = (vce.getVceOrd().getOrd() - ord <= vce.getVceBound());

        LOGGER.info("Checking FVVersionConstraint [{} vs. vce: {}]: [{}] - [{}] <= [{}] with result [{}].",
                tsCell, vce, vce.getVceOrd().getOrd(), ord, vce.getVceBound(), fvChecked);

        return new FVCheckedResult(fvChecked);
    }

    @Override
    public Map<Integer, AbstractVersionConstraint> partition(@NotNull IPartitioner partitioner, int numberOfBuckets) {
        return vcEntries.stream()
                .collect(groupingBy(vce -> partitioner.locateSiteIndexFor(vce.getVceCk(), numberOfBuckets),
                        collectingAndThen(toList(), FVVersionConstraint::new)));
    }

}
