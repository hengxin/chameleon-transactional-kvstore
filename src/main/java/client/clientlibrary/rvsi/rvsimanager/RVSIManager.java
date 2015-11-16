package client.clientlibrary.rvsi.rvsimanager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.versionconstraints.AbstractVersionConstraint;
import client.clientlibrary.transaction.QueryResults;

/**
 * @author hengxin
 * @date Created on 11-16-2015
 * 
 *       <p>
 *       Manage the rvsi specifications (of {@link AbstractRVSISpecification})
 *       related tasks, including collecting rvsi specifications, generating
 *       version constraints (of {@link AbstractVersionConstraint}), and
 *       triggering the check procedures for the version constraints.
 */
public class RVSIManager
{
	// FIXME try {@link Stream} in Java 8.
	private List<AbstractRVSISpecification> rvsi_spec_list = new ArrayList<>();
	private List<AbstractVersionConstraint> vc_list = new ArrayList<>();

	private QueryResults query_results;

	public void collectRVSISpec(AbstractRVSISpecification rvsi_spec)
	{
		this.rvsi_spec_list.add(rvsi_spec);
	}

	public void setQueryResults(QueryResults query_results)
	{
		this.query_results = query_results;
	}

	/**
	 * Generate the version constraints (of {@link AbstractVersionConstraint})
	 * according to the rvsi specifications (of
	 * {@link AbstractRVSISpecification}) collected in {@value #rvsi_spec_list}.
	 */
	public void generateVersionConstraints()
	{
		if (this.query_results != null)		// TODO try Optional in Java 8
			this.vc_list = this.rvsi_spec_list.stream()
					.map(rvsi_spec -> rvsi_spec.generateVersionConstraint(this.query_results))	// TODO try :: operator here
					.collect(Collectors.toList());
	}
	
	public boolean check()
	{
//		return this.vc_list.stream().map(AbstractVersionConstraint::check)
				
		return false;
	}
}