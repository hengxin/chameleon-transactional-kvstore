package client.clientlibrary.rvsi.rvsispec;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
public class SVSpecification extends AbstractRVSISpecification {

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
	public List<VCEntryRawInfo> extractVCEntryRawInfo(@NotNull QueryResults query_results) {
		super.vceInfos = rvsiSpecMap.entrySet().stream()
				.flatMap(rvsiSpecEntry ->
						expand(join(rvsiSpecEntry.getKey(), query_results), rvsiSpecEntry.getValue()).stream()
					)
				.collect(Collectors.toList());
		return super.vceInfos;
	}

	/**
	 * "Join" a set of {@link CompoundKey}s and a {@link QueryResults} which is a map of 
	 * {@link CompoundKey} to {@link ITimestampedCell}, by their common {@link CompoundKey}.
	 * 
	 * @param ck_set a set of {@link CompoundKey}s
	 * @param queryResults {@link QueryResults} which is a map of
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
    SortedSet<KVItem> join(@NotNull Set<CompoundKey> ck_set, @NotNull QueryResults queryResults) {
		return ck_set.stream()
			.map(ck ->
				queryResults.getTsCell(ck).map(tsCell -> new KVItem(ck, tsCell)))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toCollection(() -> new TreeSet<>(KVItem.COMPARATOR_BY_TIMESTAMP)));
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
    List<VCEntryRawInfo> expand(@NotNull SortedSet<KVItem> kv_set, final int bound) {
		if (kv_set.size() < 2)
			return new ArrayList<>();

		KVItem kv_optional = kv_set.last();
		kv_set.remove(kv_optional);
		return kv_set.stream()
				.map(kv -> new VCEntryRawInfo(kv, kv_optional, bound))
				.collect(Collectors.toList());
	}
	
	/**
	 * Generate {@link SVVersionConstraint}.
	 * The {@link Timestamp} necessary for {@link SVVersionConstraint} is extracted from 
	 * {@link VCEntryRawInfo}.
	 * 
	 * @param sts This parameter is not used.
	 */
	@NotNull
    @Override
	public AbstractVersionConstraint generateVersionConstraint(Timestamp sts) {
		List<VCEntry> vceList =  vceInfos.stream()
				.map(vce ->
					new VCEntry(vce.getVceInfoCk(),
                            vce.getVceInfoOrd(),
                            vce.getVceInfoTsOptional(),
                            vce.getVceInfoBound()))
				.collect(Collectors.toList());
		return new SVVersionConstraint(vceList);
	}

}
