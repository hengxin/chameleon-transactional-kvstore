/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.rvsi.vc.BVVersionConstraint;
import client.clientlibrary.rvsi.vc.FVVersionConstraint;
import client.clientlibrary.rvsi.vc.VCEntry;
import client.clientlibrary.rvsi.vc.VCEntryRawInfo;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;

/**
 * RVSI specifications, including k1-bv (backward view; see {@link BVSpecification}), 
 * k2-fv (forward view; see {@link FVSpecification}), and k3-sv (snapshot view; see {@link SVSpecification}).
 * <p>
 * Each specification is like: { {x} -> 1, {y,z} -> 2, {u,v,w} -> 4}, which is a map of 
 * a set of keys {@link CompoundKey} to a staleness bound.
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 */
public abstract class AbstractRVSISpecification {
	// FIXME replace HashSet by Set???
	final Map<HashSet<CompoundKey>, Integer> rvsiSpecMap = new HashMap<>();
	List<VCEntryRawInfo> vceInfos;
	
	public void addSpec(HashSet<CompoundKey> ckSet, int bound)
	{
		rvsiSpecMap.put(ckSet, bound);
	}

	/**
	 * Flatten the {@link #rvsiSpecMap}.
	 * For example, if {@link #rvsiSpecMap} is
	 * { {x,y} -> 2, {z} -> 3, {u,v,w} -> 4 },
	 * then the result flatten_map is
	 * {x -> 2, y -> 2, z -> 3, u -> 4, v -> 4, w -> 4}.
	 * 
	 * <p> This utility method is <em>only</em> for {@link BVSpecification} and {@link FVSpecification}
	 * to generate their respective {@link AbstractVersionConstraint}.
	 * {@link SVSpecification} does not use it.
	 * 
	 * <p> <b>Note:</b> The implementation using Java 8 Stream APIs is due to Tagir Valeev from StackOverflow.
	 * See <a href="http://stackoverflow.com/a/33748545/1833118">Flatten the map and associate values using Java 8 Stream APIs</a>
	 * 
	 * @return a <em>flatten</em> map representation of RVSI specifications.
	 */
    Map<CompoundKey, Integer> flattenRVSISpecMap() {
		return rvsiSpecMap.entrySet().stream()
		   .<Entry<CompoundKey, Integer>>flatMap(rvsi_spec_entry ->
		       rvsi_spec_entry.getKey().stream()
		            .map(ck -> new AbstractMap.SimpleImmutableEntry<>(ck, rvsi_spec_entry.getValue())))
		   .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	/**
	 * Extract {@link VCEntryRawInfo} from this {@link AbstractRVSISpecification} and {@link QueryResults}.
	 * 
	 * <p> This method is used by {@link BVSpecification} and {@link FVSpecification}
	 * to generate {@link VCEntry} for {@link BVVersionConstraint} and {@link FVVersionConstraint}, respectively.
	 * {@link SVSpecification} overrides this method to construct its own {@link VCEntryRawInfo}. 
	 * 
	 * <p>Basically, it <b>joins</b> two maps {@link AbstractRVSISpecification} and {@link QueryResults}
	 * by their common keys. 
	 * <p>
	 * 
	 * @example
	 * Suppose that the rvsi_spec map has been flatten as { x->2, y->2, z->3, u->4, v->4, w->4 }
	 * (see AbstractRVSISpecification#flattenRVSISpecMap()) and that the query_result_map is 
	 * { x->TC1, u->TC2}, then the result will be a list { <x,TC1,2>, <u,TC2,4> }. 
	 * 
	 * @param query_results {@link QueryResults}
	 * @return 
	 */
	public List<VCEntryRawInfo> extractVCEntryRawInfo(QueryResults query_results) {
		vceInfos = flattenRVSISpecMap().entrySet().stream()
			.<Optional<VCEntryRawInfo>> map(flatten_rvsi_spec_entry -> {
					CompoundKey ck = flatten_rvsi_spec_entry.getKey();
					int bound = flatten_rvsi_spec_entry.getValue();
					return query_results.getTsCell(ck).map(ts_cell -> new VCEntryRawInfo(ck, ts_cell, bound));
				})
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
		
		return vceInfos;
	}

	public AbstractVersionConstraint generateVersionConstraint(QueryResults query_results, Timestamp ts) {
		extractVCEntryRawInfo(query_results);
		return generateVersionConstraint(ts);
	}

	/**
	 * @param ts {@link Timestamp} to be checked against
	 * @return {@link AbstractVersionConstraint}
	 */
	public abstract AbstractVersionConstraint generateVersionConstraint(Timestamp ts);
	
	/**
	 * Utility method for both {@link BVSpecification} and {@link FVSpecification} to transform 
	 * {@link VCEntryRawInfo} to {@link VCEntry}.
	 * @param vce_info_list a list of {@link VCEntryRawInfo} to be transformed
	 * @param ts an additional {@link Timestamp} for constructing {@link VCEntry}
	 * @return a list of {@link VCEntry}
	 */
	static List<VCEntry> transform(List<VCEntryRawInfo> vce_info_list, Timestamp ts) {
		return vce_info_list.stream()
				.<VCEntry>map(vce_info -> new VCEntry(vce_info.getVceInfoCk(), vce_info.getVceInfoOrd(), ts, vce_info.getVceInfoBound()))
				.collect(Collectors.toList());
	}
	
	void setVceInfoList(List<VCEntryRawInfo> vce_info_list) { vceInfos = vce_info_list; }

}
