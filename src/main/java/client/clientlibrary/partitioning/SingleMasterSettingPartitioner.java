package client.clientlibrary.partitioning;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import client.clientlibrary.transaction.BufferedUpdates;
import kvs.compound.CompoundKey;
import kvs.compound.KVItem;

/**
 * {@link SingleMasterSettingPartitioner} is a trivial partition strategy used in the
 * single-master setting: it returns the index of the single master (0 by default) 
 * always for any {@link CompoundKey}.
 * @author hengxin
 * @date Created on Jan 3, 2016
 */
public class SingleMasterSettingPartitioner implements IPartitioner {

	/**
	 * Returns 0 (the index of the single master) always for any @param ck.
	 */
	@Override
	public int locateSiteIndexFor(CompoundKey ck, int buckets) {
		return 0;
	}

	/**
	 * Always map 0 (the index of the single master) to @param updates.
	 */
	@Override
	public Map<Integer, List<KVItem>> locateSiteIndicesFor(BufferedUpdates updates, int buckets) {
		return ImmutableMap.of(0, updates.stream().collect(Collectors.toList()));
	}

}
