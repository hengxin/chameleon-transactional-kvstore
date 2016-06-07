package client.clientlibrary.partitioning;

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
     * @param ck {@link CompoundKey} to locate
     * @param buckets not used for this partitioner
	 * @return always returns 0 (the index of the single master) for any key
	 */
	@Override
	public int locateSiteIndexFor(CompoundKey ck, int buckets) {
		return 0;
	}

}
