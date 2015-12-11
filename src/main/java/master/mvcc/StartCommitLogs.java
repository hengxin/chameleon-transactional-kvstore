package master.mvcc;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import intervaltree.IntervalTree;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * @author hengxin
 * @date 11-14-2015
 * 
 * <p> Logs of committed transactions, each of which consisting of 
 * its start-timestamp (sts), commit-timestamp (cts), and updates (of {@link BufferedUpdates}).
 * 
 * <p> {@link StartCommitLogs} will be used to check write-conflicts among transactions.
 * 
 * @licenceNote 
 * 	The {@link IntervalTree} implementation used here is credited to gds12/IntervalTree
 *  (see <a href="https://github.com/gds12/IntervalTree">gds12/IntervalTree AT GitHub</a>).
 */
@ThreadSafe
public class StartCommitLogs
{
	@GuardedBy("read_lock, write_lock")
	private IntervalTree<Timestamp, BufferedUpdates> start_commit_logs = new IntervalTree<>();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	public final Lock read_lock = this.lock.readLock();
	public final Lock write_lock = this.lock.writeLock();
	
	/**
	 * Adding a new start-commit-log of a transaction
	 * @param sts start-timestamp 
	 * @param cts commit-timestamp
	 * @param updates buffered updates
	 * @throws InterruptedException if lock synchronization fails.
	 */
	public void addStartCommitLog(Timestamp sts, Timestamp cts, BufferedUpdates updates) throws InterruptedException
	{
		try
		{
			this.write_lock.tryLock(500, TimeUnit.MILLISECONDS);
			this.start_commit_logs.put(sts, cts, updates);
		} finally
		{
			this.write_lock.unlock();
		}
	}
	
	/**
	 * Check whether the {@link ToCommitTransaction} is write-conflict-free w.r.t already committed transactions.
	 * @param tx the transaction to commit
	 * @return {@code true} if tx is write-conflict-free w.r.t committed transactions. {@code false}, otherwise.
	 * 
	 * <p>
	 * <b>TODO</b> check the side-effects on the original underlying collections. 
	 */
	public boolean wcf(ToCommitTransaction tx)
	{
		Collection<BufferedUpdates> overlapping_tx_updates;
		try
		{
			overlapping_tx_updates = this.containersOf(tx.getSts());
		} catch (InterruptedException ie)
		{
			return false;	// TODO: retry???
		} 
		
		// collect all updated keys
		Set<CompoundKey> overlapping_updated_cks = overlapping_tx_updates.stream()
				.reduce(new HashSet<CompoundKey>(), 
						(acc_cks, buffered_updates) -> { acc_cks.addAll(buffered_updates.getUpdatedCKeys()); return acc_cks; }, 
						(acc_cks_1, acc_cks_2) -> { acc_cks_1.addAll(acc_cks_2); return acc_cks_1; }
						);
				
		overlapping_updated_cks.retainAll(tx.getBufferedUpdates().getUpdatedCKeys());
		
		return overlapping_updated_cks.isEmpty();
	}

	/**
	 * @param sts start-timestamp of a transaction
	 * @return a collection of {@link BufferedUpdates} that contain @param sts
	 * @throws InterruptedException if lock synchronization fails.
	 */
	protected Collection<BufferedUpdates> containersOf(Timestamp sts) throws InterruptedException
	{
		try
		{
			this.read_lock.tryLock(500, TimeUnit.MILLISECONDS);
			return this.start_commit_logs.searchContaining(sts, sts);
		} finally
		{
			this.read_lock.unlock();
		}
	}
}
