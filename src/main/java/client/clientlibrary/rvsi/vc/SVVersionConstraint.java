package client.clientlibrary.rvsi.vc;

import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.rvsi.rvsispec.SVSpecification;
import client.clientlibrary.transaction.QueryResults;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;

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

    public SVVersionConstraint(List<VCEntry> vcEntries) { super(vcEntries); }

    /**
     * @param table  check against this {@code table}
     * @param vce  {@link VCEntry} to be checked
     * @return O_x(T_l.cts) - ord(x_j) <= k3; see the paper.
     */
    @Override
    public boolean check(AbstractTable table, VCEntry vce) {
        ITimestampedCell tsCell = table.getTimestampedCell(vce.getVceCk(), vce.getVceTs());
        long ord = tsCell.getOrdinal().getOrd();
        return (ord - vce.getVceOrd().getOrd() <= vce.getVceBound());
    }

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public Map<Integer, AbstractVersionConstraint> partition(IPartitioner partitioner, int buckets)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException("No partition method supported for SVVersionConstraint.");
    }

}
