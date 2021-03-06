package client.clientlibrary.rvsi.vc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.vc.vcresult.VCCheckedResult;
import kvs.table.AbstractTable;

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

    /**
     * Check this {@link AbstractVersionConstraint}.
     * @param table check against this {@code table}
     * @return {@code true} if this {@link AbstractVersionConstraint} is satisfied against {@code table};
     *  {@code false}, otherwise.
     *
     * @implNote Extract more common logic from subclasses
     */
	public VCCheckedResult check(AbstractTable table) {
        return vcEntries.stream()
                .map(vce -> check(table, vce))
                .reduce(VCCheckedResult.IDENTITY, VCCheckedResult::accumulate);
    }

    /**
     * Check an individual {@code vce} against {@code table}.
     * @param table  check against this {@code table}
     * @param vce  {@link VCEntry} to be checked
     * @return {@code true} if this {@code vce} is satisfied against {@code table};
     *   {@code false}, otherwise.
     */
    @NotNull
    public abstract VCCheckedResult check(AbstractTable table, VCEntry vce);

    /**
     * Partition a version constraint into multiple ones according to some
     * {@link client.clientlibrary.partitioning.IPartitioner} mechanism.
     * @param partitioner instance of {@link IPartitioner} to use
     * @param numberOfBuckets   number of buckets to put into
     * @return a map from index of {@link site.ISite} to {@link AbstractVersionConstraint}
     */
    public abstract Map<Integer, AbstractVersionConstraint> partition(IPartitioner partitioner, int numberOfBuckets);
	
	@Override
	public int hashCode() { return Objects.hashCode(vcEntries); }

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
	
	@NotNull
    @Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("VC_Entry_List", vcEntries)
				.toString();
	}

}
