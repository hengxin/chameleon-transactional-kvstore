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
	 * returns the {@link ISite} which is responsible for it.
	 * @param row {@link Row} key
	 * @param col {@link Column} key
	 * @return the {@link ISite} responsible for the given key
	 */
	public abstract ISite locateSiteFor(Row row, Column col);
	
	/**
	 * Given {@link BufferedUpdates} (which contains a collection of {@link KVItem}), the partitioner
	 * returns for each item an {@link ISite} which is responsible for it.
	 * @param updates {@link BufferedUpdates}
	 * @return a map from an {@link ISite} to the {@link KVItem}s (in @param updates) it is responsible for.
	 */
	public abstract Map<ISite, List<KVItem>> locateSitesFor(BufferedUpdates updates);
}