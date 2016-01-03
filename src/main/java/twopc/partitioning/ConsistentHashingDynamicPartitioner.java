package twopc.partitioning;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

import client.clientlibrary.transaction.BufferedUpdates;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.CompoundKey;
import kvs.compound.KVItem;

/**
 * Partitioner supporting dynamic join and exit of storage nodes.
 * Storage nodes are arranged as a distributed hash table with
 * partition placement determined by consistent hashing.
 * 
 * @implNote
 * This implementation uses {@link Hashing} of <a href="https://github.com/google/guava">google/guava@Github</a>.
 * @see <a href="https://en.wikipedia.org/wiki/Consistent_hashing">Consistent hashing@wiki</a>
 *  
 * @author hengxin
 * @date Created on Dec 31, 2015
 */
public final class ConsistentHashingDynamicPartitioner implements IPartitioner
{
	/**
	 * {@inheritDoc}
	 * @see	
	 *   <a href="http://stackoverflow.com/a/34540632/1833118">Generate HashCode for Hashing#consistentHash of google/guava</a>
	 *   at StackOverflow.
	 */
	@Override
	public int locateSiteIndexFor(CompoundKey ck, int buckets)
	{
		return this.locateSiteIndexFor(ck.getRow(), ck.getCol(), buckets);
	}
	
	@Override
	public Map<Integer, List<KVItem>> locateSiteIndicesFor(BufferedUpdates updates, int buckets)
	{
		return updates.stream()
				.collect(Collectors.groupingBy(item -> locateSiteIndexFor(item.getCK(), buckets)));
	}

	/**
	 * Utility method for {@link #locateSiteIndexFor(CompoundKey, int)}.
	 * @param r		{@link Row} key
	 * @param c		{@link Column} key
	 * @param buckets		number of buckets (i.e., master nodes)
	 * @return		the index of the site who is responsible for the key
	 */
	private int locateSiteIndexFor(Row r, Column c, int buckets)
	{
		HashCode hash_code = Hashing.murmur3_32().newHasher()
				.putString(r.getRowKey(), StandardCharsets.UTF_8)
				.putString(c.getColumnKey(), StandardCharsets.UTF_8)
				.hash();
		
		return Hashing.consistentHash(hash_code, buckets);
	}
}
