package client.clientlibrary.rvsi.vc;

import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.rvsi.rvsispec.SVSpecification;
import client.clientlibrary.transaction.QueryResults;
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

	@Override
	public boolean check(AbstractTable table) {
		// TODO Auto-generated method stub
		return false;
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
