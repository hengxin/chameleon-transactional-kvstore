package master;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import javax.jms.JMSException;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import jms.AbstractJMSParticipant;
import jms.master.JMSCommitLogPublisher;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.table.AbstractSite;
import kvs.table.MasterTable;
import master.mvcc.StartCommitLogs;
import messages.AbstractMessage;
import messages.IMessageProducer;

/**
 * Master employs an MVCC protocol to locally implement snapshot isolation (SI, for short).
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 */
public class SIMaster extends AbstractSite implements IMaster, IMessageProducer
{
	private static final Logger LOGGER = LoggerFactory.getLogger(SIMaster.class);
	private final ExecutorService exec = Executors.newCachedThreadPool();
	
	private AtomicLong ts = new AtomicLong(0);	// for generating start-timestamps and commit-timestamps; will be accessed concurrently
	private StartCommitLogs logs = new StartCommitLogs();	// commit log: each entry is composed of start-timestamp, commit-timestamp, and buffered updates of a transaction

	/**
	 * Constructor: hold a {@link MasterTable}. 
	 */
	public SIMaster()
	{
		super.table = new MasterTable();	// the underlying database in the "table" form
	}
	
	/**
	 * Start a transaction: generate and assign a new start-timestamp.
	 * @return a start-timestamp
	 * @throws InterruptedException if it fails to generate a new {@link Timestamp}, maybe due to asynchronous task failure.
	 * @throws ExecutionException if it fails to generate a new {@link Timestamp}, maybe due to asynchronous task failure.
	 */
	@Override
	public Timestamp start() throws InterruptedException, ExecutionException
	{
        // Using implicit {@link Future} to get the result; also use Java 8 Lambda expression
		long sts = exec.submit( () -> 
		{
			return this.ts.incrementAndGet();
		}).get();
		
		return new Timestamp(sts);
	}

	@Override
	public Cell read(Row row, Column col)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Try to commit a transaction:
	 * <p><ol> 
	 * <li> check version constraints; check write-conflict-free
	 * <li> generate and assign commit-timestamp
	 * <li> update start-commit-log
	 * <li> apply buffered-updates to the in-memory table
	 * <li> propagate the updates
	 * </ol><p>
	 * <b>Note:</b> (3) and (4) cannot be re-ordered!
	 *  
	 * <p> TODO Start a new thread? 
	 */
	@Override
	public boolean commit(ToCommitTransaction tx, VersionConstraintManager vc_manager)
	{
		Timestamp cts = null;
		BufferedUpdates buffered_updates = null;
		
		boolean vc_checked = vc_manager.check();
		boolean wcf_checked = false;
		boolean can_committed = false;
		
		try
		{
			if(this.logs.write_lock.tryLock(1, TimeUnit.SECONDS))
			{
				wcf_checked = this.logs.wcf(tx);
				can_committed = vc_checked && wcf_checked;
				
				if (can_committed)	// (1) check
				{
					cts = new Timestamp(this.ts.incrementAndGet());	// (2) commit-timestamp
					buffered_updates = tx.getBuffered_Updates();	
					this.logs.addStartCommitLog(tx.getSts(), cts, buffered_updates);	// (3) update start-commit-log
				}
			}
		} catch (InterruptedException ie)
		{
			ie.printStackTrace();
			can_committed = false;
		} finally
		{
			this.logs.write_lock.unlock();
		}
		

		if(can_committed)
		{
			this.table.apply(cts, buffered_updates);	// (4) apply buffered-updates to the in-memory table
			// TODO (5) propagate
		}
		
		return can_committed;
	}

	@Override
	public void send(AbstractMessage msg)
	{
		Assert.assertNotNull("Please call registerASJMSParticipant() first.", super.jmser); 

		try
		{
			((JMSCommitLogPublisher) super.jmser).publish(msg);
		} catch (JMSException jmse)
		{
			System.out.format("Fail to publish the message [%s], due to %s.", msg.toString(), jmse.getMessage());
			jmse.printStackTrace();
		}
	}

}
