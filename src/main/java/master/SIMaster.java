package master;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.transaction.RVSITransaction.Update;
import kvs.table.Cell;
import kvs.table.Column;
import kvs.table.Row;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * Master employs an MVCC protocol to locally implement SI isolation level.
 * 
 * Singleton design pattern using {@link enum}
 */
public enum SIMaster implements IMaster
{
	INSTANCE;

	private static final Logger LOGGER = LoggerFactory.getLogger(SIMaster.class);
	
	private AtomicLong ts = new AtomicLong(0);	// for generating start-timestamps and commit-timestamps; will be accessed concurrently
	
	private final ExecutorService exec = Executors.newCachedThreadPool();

	@Override
	public long start() throws InterruptedException, ExecutionException
	{
        // Using implicit {@link Future} to get the result; also use Java 8 Lambda expression
		return exec.submit( () -> 
		{
			return SIMaster.this.ts.incrementAndGet();
		}).get();
	}

	@Override
	public Cell read(Row row, Column col)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean commit(List<Update> updates)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
