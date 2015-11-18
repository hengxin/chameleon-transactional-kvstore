package client.clientlibrary.rvsi.versionconstraints;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.transaction.QueryResults;
import kvs.compound.CompoundKey;
import kvs.compound.TimestampedCell;

/**
 * @author hengxin
 * @date Created on 11-16-2015
 * 
 * <p> Representing the version constraints generated 
 * according to the {@link AbstractRVSISpecification}.
 */
public abstract class AbstractVersionConstraint
{
	private List<VersionConstraintElement> vc_element_list;
	
	public AbstractVersionConstraint(List<VersionConstraintElement> vc_element_list)
	{
		this.vc_element_list = vc_element_list;
	}
	
	public abstract boolean check();
	
	/**
	 * Extract elements from {@link AbstractRVSISpecification} and {@link QueryResults} 
	 * for generating {@link AbstractVersionConstraint}.
	 * 
	 * <p> This method will be used by {@link BVSpecification} and {@link FVSpecification}
	 * to generate their respective {@link AbstractVersionConstraint}. 
	 * 
	 * <p>Basically, it <b>joins</b> the two maps {@link AbstractRVSISpecification} and {@link QueryResults}
	 * by their common keys. 
	 * 
	 * <p>For example:
	 * suppose that the rvsi_spec_map has been flatten as { x->2, y->2, z->3, u->4, v->4, w->4 }
	 * (see AbstractRVSISpecification#flattenRVSISpecMap()) and that the query_result_map is 
	 * { x->TC1, u->TC2}, then the result will be a list { <x,TC1,2>, <u,TC2,4> }. 
	 * 
	 * @param rvsi_spec {@link AbstractRVSISpecification}
	 * @param query_result {@link QueryResults}
	 * 
	 * @return a list of triple of ({@link CompoundKey}, {@link TimestampedCell}, {@link Long})
	 */
	public static List<Triple<CompoundKey, TimestampedCell, Long>> extractVersionConstraintElements(AbstractRVSISpecification rvsi_spec, QueryResults query_results)
	{
		return rvsi_spec.flattenRVSISpecMap().entrySet().stream()
			.<Triple<CompoundKey, TimestampedCell, Long>>map(flatten_rvsi_spec_entry ->
				{
					CompoundKey ck = flatten_rvsi_spec_entry.getKey();
					return Triple.of(ck, query_results.getQueryResults().get(ck), flatten_rvsi_spec_entry.getValue());
				})
			.filter(triple -> triple.getMiddle() != null)
			.collect(Collectors.toList());
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.vc_element_list);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (o.getClass() == this.getClass()))
			return false;
		
		return CollectionUtils.isEqualCollection(this.vc_element_list, ((AbstractVersionConstraint) o).vc_element_list);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("VC_Element_List", this.vc_element_list)
				.toString();
	}
}
