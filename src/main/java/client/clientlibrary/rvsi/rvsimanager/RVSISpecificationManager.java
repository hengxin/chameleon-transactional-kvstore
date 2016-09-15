package client.clientlibrary.rvsi.rvsimanager;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.transaction.RVSITransaction;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * This {@link RVSISpecificationManager} maintains a list of {@link AbstractRVSISpecification},
 * and is mainly responsible for generating a {@link VersionConstraintManager}.
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public final class RVSISpecificationManager {
    private static final Logger LOGGER = getLogger(RVSISpecificationManager.class);

    // FIXME try {@link Stream} in Java 8 directly.
	private final List<AbstractRVSISpecification> rvsiSpecs = new ArrayList<>();

	@NotNull
    public RVSISpecificationManager collectRVSISpecification(AbstractRVSISpecification rvsiSpec) {
		rvsiSpecs.add(rvsiSpec);
        return this;
	}

	/**
	 * Generate the version constraints (of {@link AbstractVersionConstraint})
	 * according to the rvsi specifications (of
	 * {@link AbstractRVSISpecification}) collected in {@link #rvsiSpecs}.
     * FIXME: static method???
	 */
	@NotNull
    public VersionConstraintManager generateVCManager(@NotNull RVSITransaction tx) {
		List<AbstractVersionConstraint> vcList = rvsiSpecs.stream()
				.map(rvsiSpec -> rvsiSpec.generateVersionConstraint(tx.getQueryResults(), tx.getSts()))
				.collect(Collectors.toList());

		return new VersionConstraintManager(vcList);
	}

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("rvsiSpecs", rvsiSpecs)
                .toString();
    }

}