/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import client.clientlibrary.rvsi.versionconstraints.AbstractVersionConstraint;
import client.clientlibrary.transaction.QueryResults;
import kvs.compound.CompoundKey;

/**
 * @author hengxin
 * @date Created on 10-27-2015
 * 
 * <p> RVSI specifications, including k1-bv (backward view; see {@link BVSpecification}), 
 * k2-fv (forward view; see {@link FVSpecification}), and k3-sv (snapshot view; see {@link SVSpecification}).
 * Each specification is like: { {x} -> 1, {y,z} -> 2, {u,v,w} -> 4}, which is a map of 
 * a set of keys {@link CompoundKey} to an Integer staleness bound.
 */
public abstract class AbstractRVSISpecification
{
	protected Map<HashSet<CompoundKey>, Integer> rvsi_spec_map = new HashMap<>();
	
	public void addSpec(HashSet<CompoundKey> ckeys, int bound)
	{
		this.rvsi_spec_map.put(ckeys, bound);
	}

	public Map<HashSet<CompoundKey>, Integer> getSpecMap()
	{
		return this.rvsi_spec_map;
	}
	
	public abstract AbstractVersionConstraint generateVersionConstraint(QueryResults query_results);
}
