package client.clientlibrary.partitioning;

import kvs.compound.CompoundKey;

/**
 * A simple static partitioner which partitions 
 * data according to their keys (in alphabetical order). 
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public final class AlphabeticalStaticPartitioner implements IPartitioner {

	private final int buckets;
	
	public AlphabeticalStaticPartitioner(int buckets) {
		this.buckets = buckets;
	}

	@Override
	public int locateSiteIndexFor(CompoundKey ck, int buckets) {
		// TODO Not yet implemented
		return -1;
	}

}
