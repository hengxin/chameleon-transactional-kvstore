package master;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.table.AbstractTable;
import kvs.table.MasterTable;
import master.mvcc.StartCommitLogs;

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
	private final ExecutorService exec = Executors.newCachedThreadPool();
	
	private AtomicLong ts = new AtomicLong(0);	// for generating start-timestamps and commit-timestamps; will be accessed concurrently
	private AbstractTable tbl = new MasterTable();	// the underlying database in the "table" form
	private StartCommitLogs logs = new StartCommitLogs();	// commit log: each entry is composed of start-timestamp, commit-timestamp, and buffered updates of a transaction

	@Override
	public Timestamp start() throws InterruptedException, ExecutionException
	{
        // Using implicit {@link Future} to get the result; also use Java 8 Lambda expression
		long sts = exec.submit( () -> 
		{
			return SIMaster.this.ts.incrementAndGet();
		}).get();
		
		return new Timestamp(sts);
	}

	@Override
	public Cell read(Row row, Column col)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean commit(ToCommitTransaction tx, VersionConstraintManager vc_manager)
	{
		if (! this.logs.conflictCheck(tx) && vc_manager.check())
			return true;
		return false;
	}

}
