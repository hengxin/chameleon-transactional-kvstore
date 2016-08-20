package client.clientlibrary.partitioning;

import java.io.Serializable;

import kvs.compound.CompoundKey;
import site.ISite;

/**
 * A partitioner is responsible for partitioning a large
 * data set into multiple smaller data <i>shards</i>.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface IPartitioner extends Serializable {

	/**
	 * Given a {@link CompoundKey}, this partitioner returns 
	 * the index of an {@link ISite} who is responsible for the key.
	 * @param ck	key to locate
	 * @param buckets number of buckets (i.e., storage nodes)
	 * @return the index of an {@link ISite} who is responsible for the given key
	 */
    int locateSiteIndexFor(CompoundKey ck, int buckets);

}