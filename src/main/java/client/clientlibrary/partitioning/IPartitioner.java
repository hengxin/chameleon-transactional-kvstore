package client.clientlibrary.partitioning;

import java.util.Map;

import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.compound.CompoundKey;
import site.ISite;

/**
 * A partitioner is responsible for partitioning a large
 * data set into multiple smaller data <i>shards</i>.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface IPartitioner {

	/**
	 * Given a {@link CompoundKey}, this partitioner returns 
	 * the index of an {@link ISite} who is responsible for the key.
	 * @param ck	key to locate
	 * @param buckets number of buckets (i.e., storage nodes)
	 * @return the index of an {@link ISite} who is responsible for the given key
	 */
	public abstract int locateSiteIndexFor(CompoundKey ck, int buckets);
	
	/**
	 * Given a {@link ToCommitTransaction}, the partitioner decomposes it into multiple
	 * sub-{@link ToCommitTransaction}s, one for each {@link ISite} involved.
	 * This method returns a map from the index of an {@link ISite} to the sub-{@link ToCommitTransaction}
	 * it is responsible for.
	 * @param tx {@link ToCommitTransaction} to partition
	 * @param buckets	number of buckets (i.e., {@link ISite})
	 * @return a map from the index of an {@link ISite} to the sub-{@link ToCommitTransaction} it is responsible for
	 */
	public abstract Map<Integer, ToCommitTransaction> partition(ToCommitTransaction tx, int buckets);

}