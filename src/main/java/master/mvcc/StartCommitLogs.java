package master.mvcc;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import utils.intervaltree.IntervalTree;

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
 * TODO Using <a href="https://github.com/lowasser/intervaltree">lowasser/intervaltree@GitHub</a>.
 */
@ThreadSafe
public class StartCommitLogs {
	private final static Logger LOGGER = LoggerFactory.getLogger(StartCommitLogs.class);
	
	@NotNull
    @GuardedBy("readLock, writeLock")
	private IntervalTree<Timestamp, BufferedUpdates> startCommitLogs = new IntervalTree<>();

	public final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final Lock readLock = lock.readLock();
	public final Lock writeLock = lock.writeLock();
	
	/**
	 * Adding a new start-commit-log of a transaction
	 * @param sts start-timestamp 
	 * @param cts commit-timestamp
	 * @param updates buffered updates
	 */
	public void addStartCommitLog(Timestamp sts, Timestamp cts, BufferedUpdates updates) {
        writeLock.lock();
		try {
			startCommitLogs.put(sts, cts, updates);
		} finally {
			writeLock.unlock();
		}
	}
	
	/**
	 * Check whether the {@link ToCommitTransaction} is write-conflict-free
     * w.r.t already committed transactions.
     *
	 * @param tx the transaction to commit; it cannot be {@code null}
	 * @return {@code true} if tx is write-conflict-free w.r.t committed transactions;
     *  {@code false}, otherwise.
	 * 
	 * <p>
	 * <b>TODO</b> check the side-effects on the original underlying collections. 
	 */
	public boolean wcf(@NotNull ToCommitTransaction tx) {
        Collection<BufferedUpdates> overlappingTxUpdates = containersOf(tx.getSts());

        // collect all updated keys
        Set<CompoundKey> overlappingUpdatedCks = overlappingTxUpdates.parallelStream()
                .map(BufferedUpdates::getUpdatedCKeys)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        overlappingUpdatedCks.retainAll(tx.getBufferedUpdates().getUpdatedCKeys());

        return overlappingUpdatedCks.isEmpty();
	}

	/**
	 * @param sts start-timestamp of a transaction
	 * @return a collection of {@link BufferedUpdates} that contain @param sts
	 */
    @NotNull Collection<BufferedUpdates> containersOf(Timestamp sts) {
		readLock.lock();
		try {
			return startCommitLogs.searchContaining(sts, sts);
		} finally {
			readLock.unlock();
		}
	}
}
