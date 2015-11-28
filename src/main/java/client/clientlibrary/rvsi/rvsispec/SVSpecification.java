/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.rvsi.vc.VCEntryRawInfo;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;

/**
 * @author hengxin
 * @date 10-27-2015
 */
public class SVSpecification extends AbstractRVSISpecification
{

	/**
	 * 
	 * @param query_results
	 * 
	 * @example
	 * Suppose that the {@link SVSpecification} (inherited from {@link AbstractRVSISpecification})
	 * is as follows:
	 * <p>{
	 * <p>  {t, s} -> 1,
	 *   {x, y, z} -> 2,
	 *   {u, v} -> 3
	 * }
	 */
	@Override
	public void extractVCEntryRawInfo(QueryResults query_results)
	{
//		super.vce_info_list = this.rvsi_spec_map.entrySet().stream()
//			.<VCEntryRawInfo>map(rvsi_spec_entry ->
//				{
//					Map<CompoundKey, ITimestampedCell> ck_tscell_map = new HashMap<>();
//					HashSet<CompoundKey> ck_set = rvsi_spec_entry.getKey();
//					
//					ck_set.stream()
//						.map(ck ->
//						{
//							ITimestampedCell ts_cell = query_results.getTsCell(ck);
//							ck_tscell_map.put(ck, ts_cell);
//						});
//					return new VCEntryRawInfo(ck_tscell_map, rvsi_spec_entry.getValue());
//				})
//			.collect(Collectors.toList());
	}
	
	@Override
	public AbstractVersionConstraint generateVersionConstraint(Timestamp sts)
	{
		return null;
	}

}
