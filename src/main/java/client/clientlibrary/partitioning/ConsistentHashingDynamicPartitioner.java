package client.clientlibrary.partitioning;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.sun.istack.NotNull;

import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.CompoundKey;

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
public enum  ConsistentHashingDynamicPartitioner implements IPartitioner {

    INSTANCE;

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
	 * @param hr	{@link HashingRequest} to locate
	 * @return	the index of the (master) site responsible for the {@link HashingRequest} 
	 */
	private int locateSiteIndexFor(@org.jetbrains.annotations.NotNull HashingRequest hr) {
		return locateSiteIndexFor(hr.getCK().getRow(), hr.getCK().getCol(), hr.getBuckets());
	}
	
	/**
	 * Utility method for {@link #locateSiteIndexFor(CompoundKey, int)}.
	 * @param r		{@link Row} key
	 * @param c		{@link Column} key
	 * @param buckets		number of buckets (i.e., master nodes)
	 * @return		the index of the site who is responsible for the key
	 */
	private int locateSiteIndexFor(@org.jetbrains.annotations.NotNull Row r, @org.jetbrains.annotations.NotNull Column c, int buckets) {
		HashCode hash_code = Hashing.murmur3_32().newHasher()
				.putString(r.getRow(), StandardCharsets.UTF_8)
				.putString(c.getCol(), StandardCharsets.UTF_8)
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
		public boolean equals(@Nullable Object o) {
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
