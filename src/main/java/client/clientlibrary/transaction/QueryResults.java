package client.clientlibrary.transaction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

/**
 * The query results are a map of {@link CompoundKey} to {@link TimestampedCell}.
 * <p>
 * The client stores the query results and uses them, along with {@link AbstractRVSISpecification}, 
 * to generate version constraints when a transaction is about to commit.
 * <p>
 * WARNING: Although in the normal case only the single client thread manipulates the {@link QueryResults}, 
 *   we have implemented it as thread-safe. 
 *   Thus, it can be safely used in some cases where multiple threads are allowed to 
 *   concurrently access it for optimization.
 *   
 * @author hengxin
 * @date Created on 11-11-2015
 */
public class QueryResults
{
	// TODO WARNING: using multi-hashmap if multiple reads on a data item are allowed in a transaction.
	private final Map<CompoundKey, ITimestampedCell> query_results = new ConcurrentHashMap<>();
	
	public void put(CompoundKey ck, ITimestampedCell ts_cell_t)
	{
		this.query_results.put(ck, ts_cell_t);
	}
	
	public ITimestampedCell getTsCell(CompoundKey ck)
	{
		return this.query_results.get(ck);
	}
}
