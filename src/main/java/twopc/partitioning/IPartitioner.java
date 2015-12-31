package twopc.partitioning;

import java.util.List;
import java.util.Map;

import client.clientlibrary.transaction.BufferedUpdates;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.KVItem;
import site.ISite;

/**
 * A partitioner is responsible for partitioning a large
 * data set into multiple smaller data <i>shards</i>.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface IPartitioner
{
	/**
	 * Given a key ({@link Row} key + {@link Column} key), the partitioner
	 * returns the index of an {@link ISite} who is responsible for the key.
	 * @param r {@link Row} key
	 * @param c {@link Column} key
	 * @param buckets number of buckets (i.e., storage nodes)
	 * @return the index of an {@link ISite} who is responsible for the given key
	 */
	public abstract int locateSiteFor(Row r, Column c, int buckets);
	
	/**
	 * Given {@link BufferedUpdates} (which contains a collection of {@link KVItem}s), the partitioner
	 * returns for each item the index of the {@link ISite} who is responsible for storing it.
	 * @param updates {@link BufferedUpdates}
	 * @param buckets	number of buckets (i.e., storage nodes)
	 * @return a map from an index of an {@link ISite} to the {@link KVItem}s (in @param updates) it is responsible for.
	 */
	public abstract Map<Integer, List<KVItem>> locateSitesFor(BufferedUpdates updates, int buckets);
}