package client.clientlibrary.rvsi.rvsimanager;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.rvsi.vc.SVVersionConstraint;

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
	
	public VersionConstraintManager(List<AbstractVersionConstraint> vcList) {
		this.vcList = vcList;
	}
	
	/**
	 * Check whether all the {@link AbstractVersionConstraint} can be satisfied.
	 * @return <code>true</code> if all can be satisfied; <code>false</code>, otherwise.
	 */
	public boolean check() {
		return this.vcList.stream().allMatch(AbstractVersionConstraint::check);
	}

    /**
     *
     * @param buckets
     * @return
     */
    public Map<Integer, VersionConstraintManager> partition(int buckets) {
        return vcList.stream()
                .filter(vc -> ! (vc instanceof SVVersionConstraint))
                .map(vc -> vc.partition(buckets))
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .collect(groupingBy(
                        Map.Entry::getKey,
                        mapping(Map.Entry::getValue,
                                collectingAndThen(toList(), VersionConstraintManager::new)
                        )));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        VersionConstraintManager that = (VersionConstraintManager) o;

        return CollectionUtils.isEqualCollection(vcList, that.vcList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(vcList);
    }
}
