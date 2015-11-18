/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import client.clientlibrary.rvsi.versionconstraints.AbstractVersionConstraint;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Timestamp;
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
	protected Map<HashSet<CompoundKey>, Long> rvsi_spec_map = new HashMap<>();
	
	public void addSpec(HashSet<CompoundKey> ckey_set_r1, long bound)
	{
		this.rvsi_spec_map.put(ckey_set_r1, bound);
	}

	/**
	 * Flatten the {@link #rvsi_spec_map}.
	 * For example, if {@link #rvsi_spec_map} is
	 * { {x,y} -> 2, {z} -> 3, {u,v,w} -> 4 },
	 * then the result flatten_map is
	 * {x -> 2, y -> 2, z -> 3, u -> 4, v -> 4, w -> 4}.
	 * 
	 * <p> This utility method is <em>only</em> for {@link BVSpecification} and {@link FVSpecification}
	 * to generate their respective {@link AbstractVersionConstraint}.
	 * {@SVSpecification} does not use it.
	 * 
	 * <p> <b>Note:</b> The implementation using Java 8 Stream APIs is due to Tagir Valeev from StackOverflow.
	 * See <a href="http://stackoverflow.com/a/33748545/1833118">Flatten the map and associate values using Java 8 Stream APIs</a>
	 * 
	 * @return a <em>flatten</em> map representation of RVSI specifications.
	 */
	public Map<CompoundKey, Long> flattenRVSISpecMap()
	{
		return this.rvsi_spec_map.entrySet().stream()
		   .<Entry<CompoundKey, Long>>flatMap(rvsi_spec_entry -> 
		       rvsi_spec_entry.getKey().stream()
		            .map(ck -> new AbstractMap.SimpleImmutableEntry<>(ck, rvsi_spec_entry.getValue())))
		   .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		       
//		Map<CompoundKey, Integer> flatten_map = new HashMap<>();
//		this.rvsi_spec_map.entrySet().forEach(
//				rvsi_spec_entry -> 
//				{rvsi_spec_entry.getKey().forEach(
//						compound_key -> 
//						{flatten_map.put(compound_key, rvsi_spec_entry.getValue());}
//						);}
//				);
//		return flatten_map;
	}
	
	/**
	 * Generate {@link AbstractVersionConstraint} according to the {@link QueryResults} 
	 * and (possibly) the start-timestamp of the transaction.  
	 * @param sts start-timestamp
	 * @param query_results {@link QueryResults}
	 * @return {@link AbstractVersionConstraint}
	 */
	public abstract AbstractVersionConstraint generateVersionConstraint(Timestamp sts, QueryResults query_results);
}
