package client.clientlibrary.rvsi.vc;

import java.util.List;
import java.util.Map;

import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.transaction.QueryResults;

/**
 * Snapshot-view version constraint generated according to {@link FVSpecification}
 * and {@link QueryResults}.
 * 
 * @see {@link FVSpecification}
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public final class SVVersionConstraint extends AbstractVersionConstraint {

	public SVVersionConstraint(List<VCEntry> vcEntries) {
		super(vcEntries);
	}

	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return false;
	}

    /**
     * @throws UnsupportedOperationException
     */
    @Override
    public Map<Integer, AbstractVersionConstraint> partition(int buckets) {
        throw new UnsupportedOperationException("No partition method supported for SVVersionConstraint.");
    }

}
