/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.rvsi.vc.SVVersionConstraint;
import client.clientlibrary.rvsi.vc.VCEntry;
import client.clientlibrary.rvsi.vc.VCEntryRawInfo;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.KVItem;

/**
 * @author hengxin
 * @date 10-27-2015
 */
public class SVSpecification extends AbstractRVSISpecification
{

	/**
	 * Extract {@link VCEntryRawInfo} for {@link SVVersionConstraint}. Each one
	 * consists of two {@link KVItem}s and a staleness bound.
	 * 
	 * @param query_results
	 *            {@link QueryResults}
	 * 
	 * @example Suppose that the {@link SVSpecification} (inherited from
	 *          {@link AbstractRVSISpecification}) is:
	 *          <p>
	 *          { {t, s} -> 1, {w, x, y, z} -> 2, {u, v} -> 3 }
	 *          <p>
	 *          and the {@link QueryResults} is:
	 *          <p>
	 *          { t -> Cell_t, x -> Cell_x, y -> Cell_y, z -> Cell_z, u -> Cell_u, v -> Cell_v },
	 *          <p>
	 *          then the {@link VCEntryRawInfo} extracted should be as follows:
	 *          <p>
	 *          <ol>
	 *          <li>[ vce_info_kv = {x, Cell_x}, vce_info_kv_optional = {z, Cell_z}, vce_info_bound = 2]
	 *          <li>[ vce_info_kv = {y, Cell_y}, vce_info_kv_optional = {z, Cell_z}, vce_info_bound = 2] 
	 *          <li>[ vce_info_kv = {u, Cell_u}, vce_info_kv_optional = {v, Cell_v}, vce_info_bound = 3]
	 *          </ol>
	 *          <p>
	 *          Note that [vce_info_kv = {t, Cell_t}, 3] is illegal to be an {@link SVVersionConstraint}.
	 *          We also assume that "Cell_x < Cell_y < Cell_z" when sorted by their {@link Timestamp}s.
	 * 
	 */
	@Override
	public List<VCEntryRawInfo> extractVCEntryRawInfo(QueryResults query_results)
	{
		super.vce_info_list = this.rvsi_spec_map.entrySet().stream()
				.<VCEntryRawInfo>flatMap(rvsi_spec_entry ->
						this.expand(this.join(rvsi_spec_entry.getKey(), query_results), rvsi_spec_entry.getValue()).stream()
					)
				.collect(Collectors.toList());
		return super.vce_info_list;
	}

	/**
	 * "Join" a set of {@link CompoundKey}s and a {@link QueryResults} which is a map of 
	 * {@link CompoundKey} to {@link ITimestampedCell}, by their common {@link CompoundKey}.
	 * 
	 * @param ck_set a set of {@link CompoundKey}s
	 * @param query_results {@link QueryResults} which is a map of 
	 *   {@link CompoundKey} to {@link ITimestampedCell}.
	 * @return a </em>sorted</em> set of {@link KVItem}s, 
	 *   each of which is a pair of {@link CompoundKey} and {@link ITimestampedCell}.
	 * 
	 * @example
	 * Suppose that the set of {@link CompoundKey}s are:
	 * <p>
	 *   { w, x, y, z },
	 * <p>
	 * and the {@link QueryResults} is:
	 * <p>
	 *   { t -> Cell_t, x -> Cell_x, y -> Cell_y, z -> Cell_z, u -> Cell_u, v -> Cell_v },
	 * <p>
	 * then the result is a <em>sorted</em> set of {@link KVItem}s (sorted by their {@link ITimestampedCell}s):
	 * <p>
	 *   { [x, Cell_x], [y, Cell_y], [z, Cell_z] }.
	 * <p>
	 */
	protected SortedSet<KVItem> join(Set<CompoundKey> ck_set, QueryResults query_results)
	{
		return ck_set.stream()
			.<KVItem>map(ck ->
			{
				ITimestampedCell ts_cell = query_results.getTsCell(ck);
				return (ts_cell == null) ? null : new KVItem(ck, ts_cell);
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toCollection(() -> new TreeSet<KVItem>()));
	}

	/**
	 * Expand a sorted sort of {@link KVItem}s into a list of {@link VCEntryRawInfo} structures.
	 * <p>
	 * An {@link SVSpecification} involves a set of {@link KVItem}s and a staleness bound.
	 * It implicitly indicates that <em>every two</em> of them are bounded.
	 * This method aims to explicitly enumerate them, in the form of {@link VCEntryRawInfo} structures.
	 * See the example below. 
	 * 
	 * @param kv_set a sorted set of {@link KVItem}s
	 * @param bound staleness bound allowed
	 * @return a list of {@link VCEntryRawInfo}, used for generating {@link SVVersionConstraint}
	 * 
	 * @example
	 * Suppose the sorted set of {@link KVItem}s is:
	 * <p>
	 *   { [x, Cell_x], [y, Cell_y], [z, Cell_z] }, 
	 *   (Note that we have assumed that "Cell_x < Cell_y < Cell_z" when sorted by their {@link Timestamp}s).
	 * <p>
	 * and the staleness bound is 2,
	 * then we obtain a list of two {@link VCEntryRawInfo}:
	 * <p>
	 * <ul>
	 * <li>[ vce_info_kv = {x, Cell_x}, vce_info_kv_optional = {z, Cell_z}, vce_info_bound = 2]
	 * <li>[ vce_info_kv = {y, Cell_y}, vce_info_kv_optional = {z, Cell_z}, vce_info_bound = 2] 
	 * <p>
	 */
	protected List<VCEntryRawInfo> expand(SortedSet<KVItem> kv_set, final long bound)
	{
		if (kv_set.size() < 2)
			return new ArrayList<VCEntryRawInfo>();

		KVItem kv_optional = kv_set.last();
		kv_set.remove(kv_optional);
		return kv_set.stream()
				.<VCEntryRawInfo>map(kv -> new VCEntryRawInfo(kv, kv_optional, bound))
				.collect(Collectors.toList());
	}
	
	/**
	 * Generate {@link SVVersionConstraint}.
	 * The {@link Timestamp} necessary for {@link SVVersionConstraint} is extracted from 
	 * {@link VCEntryRawInfo}.
	 * 
	 * @param sts This parameter is not used.
	 */
	@Override
	public AbstractVersionConstraint generateVersionConstraint(Timestamp sts)
	{
		List<VCEntry> vce_list =  super.vce_info_list.stream()
				.<VCEntry>map(vce_info -> 
					new VCEntry(vce_info.getVceInfoCk(), vce_info.getVceInfoOrd(), vce_info.getVceInfoTsOptional(), vce_info.getVceInfoBound()))
				.collect(Collectors.toList());
		return new SVVersionConstraint(vce_list);
	}

}
