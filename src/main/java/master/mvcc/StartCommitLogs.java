package master.mvcc;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import intervaltree.IntervalTree;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;

/**
 * @author hengxin
 * @date 11-14-2015
 * 
 * <p> Logs of committed transactions, each of which consisting of 
 * its start-timestamp (sts), commit-timestamp (cts), and updates (of {@link BufferedUpdates}).
 * 
 * <p> {@link StartCommitLogs} will be used to check write-conflicts among transactions.
 * 
 * <b>Important note:</b> The {@link IntervalTree} implementation used here is credited to gds12/IntervalTree
 * (see <a href="https://github.com/gds12/IntervalTree">gds12/IntervalTree AT GitHub</a>).
 */
public class StartCommitLogs
{
	private IntervalTree<Timestamp, BufferedUpdates> start_commit_logs = new IntervalTree<>();
	
	private final ReadWriteLock monitor = new ReentrantReadWriteLock();
	private final Lock read_lock = this.monitor.readLock();
	public final Lock write_lock = this.monitor.writeLock();
	
	/**
	 * adding a new start-commit-log of a transaction
	 * @param sts start-timestamp 
	 * @param cts commit-timestamp
	 * @param updates buffered updates
	 */
	public void addStartCommitLog(Timestamp sts, Timestamp cts, BufferedUpdates updates)
	{
		this.write_lock.lock();
		try
		{
			this.start_commit_logs.put(sts, cts, updates);
		} finally
		{
			this.write_lock.unlock();
		}
	}
	
	/**
	 * Check whether the {@link ToCommitTransaction} is write-conflict-free w.r.t already committed transactions.
	 * @param tx the transaction to commit
	 * @return <code>true</code>, if tx is write-conflict-free w.r.t committed transactions. <code>false</code>, otherwise.
	 * 
	 * <p>
	 * <b>TODO</b> check the side-effects on the original underlying collections. 
	 */
	public boolean wcf(ToCommitTransaction tx)
	{
		Collection<BufferedUpdates> overlapping_tx_updates = this.containersOf(tx.getSts()); 
		
		// collect all updated keys
		Set<CompoundKey> overlapping_updated_cks = overlapping_tx_updates.stream()
				.reduce(new HashSet<CompoundKey>(), 
						(acc_cks, buffered_updates) -> { acc_cks.addAll(buffered_updates.getUpdatedCKeys()); return acc_cks; }, 
						(acc_cks_1, acc_cks_2) -> { acc_cks_1.addAll(acc_cks_2); return acc_cks_1; }
						);
				
		overlapping_updated_cks.retainAll(tx.getBuffered_Updates().getUpdatedCKeys());
		
		return overlapping_updated_cks.isEmpty();
	}

	/**
	 * @param sts start-timestamp of a transaction
	 * @return a collection of {@link BufferedUpdates} that contain @param sts
	 */
	protected Collection<BufferedUpdates> containersOf(Timestamp sts)
	{
		this.read_lock.lock();
		try
		{
			return this.start_commit_logs.searchContaining(sts, sts);
		} finally
		{
			this.write_lock.unlock();
		}
	}
}
