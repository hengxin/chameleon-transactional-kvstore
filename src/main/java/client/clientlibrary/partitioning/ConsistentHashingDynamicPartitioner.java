package client.clientlibrary.partitioning;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.sun.istack.NotNull;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
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
public final class ConsistentHashingDynamicPartitioner implements IPartitioner {

	/**
	 * Cache for consistent hashing.
	 * @see
	 * 	<a href="https://github.com/google/guava/wiki/CachesExplained">CachesExplained@google/guava</a>
	 */
	private final LoadingCache<HashingRequest, Integer> hash_caches = CacheBuilder.newBuilder()
			.maximumSize(1000)	// FIXME making it a configuration parameter
			.build(
				new CacheLoader<HashingRequest, Integer>() {
					@Override
					public Integer load(HashingRequest hr) {
						return locateSiteIndexFor(hr);
					}
				});

	/**
	 * {@inheritDoc}
	 * @see	
	 *   <a href="http://stackoverflow.com/a/34540632/1833118">Generate HashCode for Hashing#consistentHash of google/guava</a>
	 *   at StackOverflow.
	 */
	@Override
	public int locateSiteIndexFor(CompoundKey ck, int buckets) {
		return this.hash_caches.getUnchecked(new HashingRequest(ck, buckets));
	}
	
	/**
	 * {@inheritDoc}
	 * FIXME to improve the lambda expression
	 */
	public Map<Integer, ToCommitTransaction> partition(ToCommitTransaction tx, int buckets) {
		final Timestamp sts = tx.getSts();
		return this.locateSiteIndicesFor(tx.getBufferedUpdates(), buckets)
			.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey, 
									entry -> new ToCommitTransaction(sts, new BufferedUpdates(entry.getValue()))));
	}

	/**
	 * TODO try {@link LoadingCache#getAll(Iterable)} 
	 * in <a href="https://github.com/google/guava/wiki/CachesExplained">CachesExplained</a>
	 * and multi-threading.
	 */
	protected Map<Integer, List<KVItem>> locateSiteIndicesFor(BufferedUpdates updates, int buckets) {
		return updates.stream()
				.collect(Collectors.groupingBy(item -> locateSiteIndexFor(item.getCK(), buckets)));
	}

	/**
	 * @param hr	{@link HashsingRequest} to locate
	 * @return	the index of the (master) site responsible for the {@link HashingRequest} 
	 */
	private int locateSiteIndexFor(HashingRequest hr) {
		return this.locateSiteIndexFor(hr.getCK().getRow(), hr.getCK().getCol(), hr.getBuckets());
	}
	
	/**
	 * Utility method for {@link #locateSiteIndexFor(CompoundKey, int)}.
	 * @param r		{@link Row} key
	 * @param c		{@link Column} key
	 * @param buckets		number of buckets (i.e., master nodes)
	 * @return		the index of the site who is responsible for the key
	 */
	private int locateSiteIndexFor(Row r, Column c, int buckets) {
		HashCode hash_code = Hashing.murmur3_32().newHasher()
				.putString(r.getRowKey(), StandardCharsets.UTF_8)
				.putString(c.getColumnKey(), StandardCharsets.UTF_8)
				.hash();
		
		return Hashing.consistentHash(hash_code, buckets);
	}
	
	/**
	 * Wrapper class of {@link CompoundKey} and {@link #buckets}; 
	 * used as keys in caches of the consistent hashing scheme.
	 * @author hengxin
	 * @date Created on Jan 3, 2016
	 */
	private static final class HashingRequest {

		private final CompoundKey ck;
		private final int buckets;
		
		public HashingRequest(@NotNull final CompoundKey ck, @NotNull final int buckets) {
			this.ck = ck;
			this.buckets = buckets;
		}
		
		public CompoundKey getCK() {
			return ck;
		}
		
		public int getBuckets() {
			return buckets;
		}
		
		@Override
		public int hashCode() {
			return Objects.hash(this.ck, this.buckets);
		}
		
		@Override
		public boolean equals(Object o) {
			if(o == this)
				return true;
			if(o == null)
				return false;
			if(o.getClass() != this.getClass())
				return false;
			
			HashingRequest that = (HashingRequest) o;
			return Objects.equals(this.ck, that.ck) 
					&& Objects.equals(this.buckets, that.buckets);
		}
		
		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this)
					.addValue(this.ck)
					.add("buckets", this.buckets)
					.toString();
		}
	}
	
}
