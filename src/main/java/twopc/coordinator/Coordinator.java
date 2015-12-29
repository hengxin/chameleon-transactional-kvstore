package twopc.coordinator;

import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.compound.KVItem;
import site.ISite;
import twopc.partitioning.AlphabeticalStaticPartitioner;

/**
 * Coordinator of 2PC protocol.
 * <p>
 * The coordinator executes an optimized 2PC protocol
 * with "early commit notification".
 * @author hengxin
 * @date Created on Dec 27, 2015
 * 
 * 
 */
public final class Coordinator implements ICoordinator
{
	private final ExecutorService exec = Executors.newCachedThreadPool();
	
	/**
	 * {@link #prepared_decisions} and {@link #committed_decisions}:
	 * Shared states among the coordinator (encapsulating a phaser) and the participants.
	 * They collect the decisions of all the participants during the "PREPARE" phase 
	 * and the "COMMIT" phase, respectively.
	 * 
	 * @see	#committed
	 * 
	 * @implNote
	 * There is no AtomicBooleanArray; using AtomicBoolean[].
	 * {@link AtomicIntegerArray} is also OK.
	 * {@link BitSet} would be the most suitable one if it is thread-safe.
	 * (Note: There is a [pitestrunner/AtomicBitSet]; but I don't want to introduce a dependence.)
	 */
	protected final AtomicBoolean[] prepared_decisions;
	protected final AtomicBoolean[] committed_decisions;
	
	/**
	 * {@link #committed}: 
	 * Shared states among the coordinator (encapsulating a phaser) and the participants.
	 * It indicates the commit/abort decision for the "COMMIT" phase, and is computed 
	 * based on {@link #prepared_decisions}.
	 * 
	 * @see #prepared_decisions and {@link #committed_decisions}
	 */
	protected volatile boolean committed = false;

	private final Phaser phaser;

	private final ToCommitTransaction tx;
	private final VersionConstraintManager vcm;

	private final Map<ISite, List<KVItem>> site_items_map;
	private final int count;
	
	public Coordinator(final ToCommitTransaction tx, final VersionConstraintManager vcm)
	{
		this.phaser = new CommitPhaser(this);
		
		this.tx = tx;
		this.vcm = vcm;

		this.site_items_map = AlphabeticalStaticPartitioner.INSTANCE.locateSitesFor(tx.getBufferedUpdates());
		
		this.count = site_items_map.size();
		this.prepared_decisions = new AtomicBoolean[count];	
		this.committed_decisions = new AtomicBoolean[count];
	}

	@Override
	public boolean execute2PC()
	{
		IntStream.range(0, count).parallel().forEach(id -> 
			exec.submit(new ToCommitTask(this, id, this.phaser, null, this.tx, this.vcm)));	// @param participant (replace {@code null} here)
		
		// TODO Auto-generated method stub
		return false;
	}
}
