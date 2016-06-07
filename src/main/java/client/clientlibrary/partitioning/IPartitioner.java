package client.clientlibrary.partitioning;

import java.util.Map;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
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
    int locateSiteIndexFor(CompoundKey ck, int buckets);
	
	/**
	 * Given a {@link ToCommitTransaction}, the partitioner decomposes it into multiple
	 * sub-{@link ToCommitTransaction}s, one for each {@link ISite} involved.
     *
	 * @param tx {@link ToCommitTransaction} to partition
	 * @param buckets	number of buckets (i.e., {@link ISite})
	 * @return a map from the index of an {@link ISite} to the sub-{@link ToCommitTransaction} it is responsible for
	 */
    Map<Integer, ToCommitTransaction> partition(ToCommitTransaction tx, int buckets);

    /**
     * Given a {@link VersionConstraintManager}, the partitioner decomposes it into multiple
     * sub-{@link VersionConstraintManager}s, one for each {@link ISite} involved.
     *
     * @param vcm {@link VersionConstraintManager} to partition
     * @param buckets	number of buckets (i.e., {@link ISite})
     * @return a map from the index of an {@link ISite} to the sub-{@link VersionConstraintManager} it is responsible for
     */
//    Map<Integer, VersionConstraintManager> partition(VersionConstraintManager vcm, int buckets);

}