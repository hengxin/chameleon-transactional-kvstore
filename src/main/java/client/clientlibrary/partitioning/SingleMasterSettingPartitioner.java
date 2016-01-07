package client.clientlibrary.partitioning;

import java.util.Map;

import com.google.common.collect.ImmutableMap;

import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.compound.CompoundKey;

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
	 * Always map 0 (the index of the single master) to @param tx.
	 */
	@Override
	public Map<Integer, ToCommitTransaction> partition(ToCommitTransaction tx, int buckets) {
		return ImmutableMap.of(0, tx);
	}

}
