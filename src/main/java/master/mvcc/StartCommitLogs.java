package master.mvcc;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

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
 * @licenceNote 
 * 	The {@link IntervalTree} implementation used here is credited to gds12/IntervalTree
 *  (see <a href="https://github.com/gds12/IntervalTree">gds12/IntervalTree AT GitHub</a>).
 *
 * todo Using <a href="https://github.com/lowasser/intervaltree">lowasser/intervaltree@GitHub</a>.
 */
@ThreadSafe
public class StartCommitLogs {
	private final static Logger LOGGER = LoggerFactory.getLogger(StartCommitLogs.class);
	
	@GuardedBy("read_lock, writeLock")
	private IntervalTree<Timestamp, BufferedUpdates> start_commit_logs = new IntervalTree<>();

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	public final Lock read_lock = this.lock.readLock();
	public final Lock writeLock = this.lock.writeLock();
	
	/**
	 * Adding a new start-commit-log of a transaction
	 * @param sts start-timestamp 
	 * @param cts commit-timestamp
	 * @param updates buffered updates
	 */
	public void addStartCommitLog(Timestamp sts, Timestamp cts, BufferedUpdates updates) 
	{
		this.writeLock.lock();
		try
		{
			this.start_commit_logs.put(sts, cts, updates);
		} finally
		{
			this.writeLock.unlock();
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
		Collection<BufferedUpdates> overlapping_tx_updates = this.containersOf(tx.getSts()); 
		
		// collect all updated keys
		Set<CompoundKey> overlapping_updated_cks = overlapping_tx_updates.parallelStream()
				.map(update -> update.getUpdatedCKeys())
				.flatMap(Set::stream)
				.collect(Collectors.toSet());
				
		overlapping_updated_cks.retainAll(tx.getBufferedUpdates().getUpdatedCKeys());
		
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
			this.read_lock.unlock();
		}
	}
}
