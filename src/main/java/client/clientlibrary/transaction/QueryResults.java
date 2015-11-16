package client.clientlibrary.transaction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import kvs.compound.CompoundKey;
import kvs.compound.TimestampedCell;

/**
 * @author hengxin
 * @date Created on 11-11-2015
 * 
 * The client stores the query results and uses them, along with {@link AbstractRVSISpecification}, 
 * to compute the version constraints when the transaction is about to commit.
 * 
 * WARNING: Although in the normal case only the single client thread manipulates the {@link QueryResults}, 
 *   we implemented it as thread-safe. 
 *   Thus, it can be safely used in some cases where multiple threads are allowed to 
 *   concurrently access it for optimization.
 */
public class QueryResults
{
	// TODO WARNING: using multi-hashmap if multiple reads on a data item are allowed in a transaction.
	private final Map<CompoundKey, TimestampedCell> query_results = new ConcurrentHashMap<>();
	
	/**
	 * Store a new query result.
	 * @param ck a {@link CompoundKey}
	 * @param tc a {@link TimestampedCell}
	 */
	public void put(CompoundKey ck, TimestampedCell tc)
	{
		this.query_results.put(ck, tc);
	}
}
