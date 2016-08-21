package client.clientlibrary.rvsi.vc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.apache.commons.collections4.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.IPartitioner;

/**
 * Each {@link AbstractVersionConstraint} consists of a list of {@link VCEntry}s.
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public abstract class AbstractVersionConstraint implements Serializable {
    private static final long serialVersionUID = -3517827362703797614L;

    final List<VCEntry> vcEntries;
	
	public AbstractVersionConstraint(List<VCEntry> vcEntries) { this.vcEntries = vcEntries; }
	
	public abstract boolean check();

    /**
     * Partition a version constraint into multiple ones according to some
     * {@link client.clientlibrary.partitioning.IPartitioner} mechanism.
     * @param partitioner instance of {@link IPartitioner} to use
     * @param buckets
     * @return a map from index of {@link site.ISite} to {@link AbstractVersionConstraint}
     */
    public abstract Map<Integer, AbstractVersionConstraint> partition(IPartitioner partitioner, int buckets);
	
	@Override
	public int hashCode() {
		return Objects.hashCode(vcEntries);
	}

    @Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null || o.getClass() != this.getClass())
			return false;

		AbstractVersionConstraint that = (AbstractVersionConstraint) o;
		// isEqualCollection() (on List) requires its element class override hashCode() and equals().
		return CollectionUtils.isEqualCollection(vcEntries, that.vcEntries);
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("VC_Entry_List", vcEntries)
				.toString();
	}
}
