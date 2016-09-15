package client.clientlibrary.rvsi.rvsimanager;

import com.google.common.base.MoreObjects;

import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import kvs.table.AbstractTable;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * This {@link VersionConstraintManager} maintains 
 * a list of {@link AbstractVersionConstraint}, and
 * wraps their individual check() procedures in a single method. 
 *  
 * @author hengxin
 * @date Created on 11-17-2015
 */
public final class VersionConstraintManager implements Serializable {
	private static final long serialVersionUID = -4878469768601673830L;
    private static final Logger LOGGER = LoggerFactory.getLogger(VersionConstraintManager.class);

	private final List<AbstractVersionConstraint> vcList;
	
	public VersionConstraintManager(List<AbstractVersionConstraint> vcList) { this.vcList = vcList; }
	
	/**
	 * Check whether all the {@link AbstractVersionConstraint} can be satisfied.
	 * @return <code>true</code> if all can be satisfied; <code>false</code>, otherwise.
	 */
	public boolean check(AbstractTable table) {
		return vcList.stream().allMatch(vc -> vc.check(table));
	}

    /**
     * Partition this {@link VersionConstraintManager} into multiple ones, according to {@link IPartitioner}.
     *
     * @param partitioner instance of {@link IPartitioner} to use
     * @param buckets   number of buckets in partitioning
     * @return  a map from a site index to a {@link VersionConstraintManager}
     */
    public Map<Integer, VersionConstraintManager> partition(IPartitioner partitioner, int buckets) {
        return vcList.stream()
//                .filter(vc -> ! (vc instanceof SVVersionConstraint))  % commented out by hengxin (2016-09-06)
                .map(vc -> vc.partition(partitioner, buckets))
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(groupingBy(
                        Map.Entry::getKey,
                        mapping(Map.Entry::getValue,
                                collectingAndThen(toList(), VersionConstraintManager::new)
                        )));
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VersionConstraintManager that = (VersionConstraintManager) o;

        return CollectionUtils.isEqualCollection(vcList, that.vcList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(vcList);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(vcList)
                .toString();
    }

}
