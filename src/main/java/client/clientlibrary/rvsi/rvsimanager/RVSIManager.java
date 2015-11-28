package client.clientlibrary.rvsi.rvsimanager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.transaction.RVSITransaction;

/**
 * Manage the rvsi specifications (of {@link AbstractRVSISpecification}) related
 * tasks, including collecting rvsi specifications and generating version
 * constraints (of {@link AbstractVersionConstraint}).
 * 
 * @author hengxin
 * @date Created on 11-16-2015
 */
public class RVSIManager
{
	// FIXME try {@link Stream} in Java 8 directly.
	private List<AbstractRVSISpecification> rvsi_spec_list = new ArrayList<>();

	public void collectRVSISpecification(AbstractRVSISpecification rvsi_spec)
	{
		this.rvsi_spec_list.add(rvsi_spec);
	}

	/**
	 * Generate the version constraints (of {@link AbstractVersionConstraint})
	 * according to the rvsi specifications (of
	 * {@link AbstractRVSISpecification}) collected in {@value #rvsi_spec_list}.
	 */
	public VersionConstraintManager generateVersionConstraintManager(RVSITransaction tx)
	{
		List<AbstractVersionConstraint> vc_list = this.rvsi_spec_list.stream()
				.map(rvsi_spec -> rvsi_spec.generateVersionConstraint(tx.getQueryResults(), tx.getSts())) 
				.collect(Collectors.toList());
		return new VersionConstraintManager(vc_list);
	}
}