/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils.Null;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.rvsi.vc.SVVersionConstraint;
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
	 *          { {t, s} -> 1, {x, y, z} -> 2, {u, v} -> 3 }
	 *          <p>
	 *          and the {@link QueryResults} is:
	 *          <p>
	 *          { t -> Cell_t, x -> Cell_x, y -> Cell_y, u -> Cell_u, v -> Cell_v },
	 *          <p>
	 *          then the {@link VCEntryRawInfo} extracted should be as follows:
	 *          <p>
	 *          <ol>
	 *          <li>[ vce_info_kv = {x, Cell_x}, vce_info_kv_optional = {y, Cell_y}, vce_info_bound = 2]
	 *          <li>[ vce_info_kv = {u, Cell_u}, vce_info_kv_optional = {v, Cell_v}, vce_info_bound = 3]
	 *          </ol>
	 *          <p>
	 *          Note that [vce_info_kv = {t, Cell_t}, 3] is illegal to be an {@link SVVersionConstraint}.
	 * 
	 */
	@Override
	public void extractVCEntryRawInfo(QueryResults query_results)
	{
		super.vce_info_list = this.rvsi_spec_map.entrySet().stream()
				.<VCEntryRawInfo> map(rvsi_spec_entry ->
					{
						// TODO
						return null;
					})
				.collect(Collectors.toList());
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
	 *   { x, y, z },
	 * <p>
	 * and the {@link QueryResults} is:
	 * <p>
	 *   { t -> Cell_t, x -> Cell_x, y -> Cell_y, u -> Cell_u, v -> Cell_v },
	 * <p>
	 * then the result is a <em>sorted</em> set of {@link KVItem}s (sorted by their {@link ITimestampedCell}s):
	 * <p>
	 *   { [x, Cell_x], [y, Cell_y] }.
	 * <p>
	 */
	private SortedSet<KVItem> join(HashSet<CompoundKey> ck_set, QueryResults query_results)
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
	 * Expand 
	 * @param kv_set
	 * @param bound
	 * @return
	 */
	private List<VCEntryRawInfo> expand(SortedSet<KVItem> kv_set, final long bound)
	{
		KVItem kv_optional = kv_set.last();
		
		return kv_set.stream()
				.<VCEntryRawInfo>map(kv -> new VCEntryRawInfo(kv, kv_optional, bound))
				.collect(Collectors.toList());
	}
	
	@Override
	public AbstractVersionConstraint generateVersionConstraint(Timestamp sts)
	{
		return null;
	}

}
