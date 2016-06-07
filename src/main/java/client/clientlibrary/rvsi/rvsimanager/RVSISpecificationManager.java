package client.clientlibrary.rvsi.rvsimanager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.transaction.RVSITransaction;

/**
 * This {@link RVSISpecificationManager} maintains a list of {@link AbstractRVSISpecification},
 * and is mainly responsible for generating a {@link VersionConstraintManager}.
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public final class RVSISpecificationManager {
	// FIXME try {@link Stream} in Java 8 directly.
	private final List<AbstractRVSISpecification> rvsiSpecs = new ArrayList<>();

	public void collectRVSISpecification(AbstractRVSISpecification rvsi_spec) {
		rvsiSpecs.add(rvsi_spec);
	}

	/**
	 * Generate the version constraints (of {@link AbstractVersionConstraint})
	 * according to the rvsi specifications (of
	 * {@link AbstractRVSISpecification}) collected in {@link #rvsiSpecs}.
     * FIXME: static method???
	 */
	public VersionConstraintManager generateVCManager(RVSITransaction tx) {
		List<AbstractVersionConstraint> vc_list = rvsiSpecs.stream()
				.map(rvsi_spec -> rvsi_spec.generateVersionConstraint(tx.getQueryResults(), tx.getSts()))
				.collect(Collectors.toList());
		return new VersionConstraintManager(vc_list);
	}
}