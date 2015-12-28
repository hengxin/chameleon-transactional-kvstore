package twopc.coordinator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;

/**
 * Coordinator of 2PC protocol.
 * <p>
 * The coordinator executes an optimized 2PC protocol
 * with "early commit notification".
 * @author hengxin
 * @date Created on Dec 27, 2015
 * 
 * TODO at the client side or at the master side???
 */
public final class Coordinator implements ICoordinator
{
	private final ExecutorService exec = Executors.newCachedThreadPool();
	
	/**
	 * {@link #decisions}:
	 * One of the two shared states among the coordinator (encapsulating a phaser) and the participants.
	 * It collects the decisions of all the participants during both the "PREPARE" phase and the "COMMIT" phase.
	 * @see	#committed
	 * 
	 * @implNote
	 * There is no AtomicBooleanArray; using AtomicBoolean[].
	 * {@link AtomicIntegerArray} is also OK.
	 * {@link BitSet} would be the most suitable one if it is thread-safe.
	 * (Note: There is a [pitestrunner/AtomicBitSet]; but I don't want to introduce a dependence.)
	 */
	protected AtomicBoolean[] decisions;
	
	/**
	 * {@link #committed}: 
	 * One of the two shared states among the coordinator (encapsulating a phaser) and the participants.
	 * It indicates the commit/abort decision for the "COMMIT" phase.
	 */
	protected volatile boolean committed = false;

	private final Phaser phaser;

//	private final ToCommitTransaction tx;
//	private final VersionConstraintManager vcm;
	
	public Coordinator()
	{
		this.phaser = new CommitPhaser(this);
	}

	@Override
	public boolean coorCommit2PC(ToCommitTransaction tx, VersionConstraintManager vcm)
	{
		
		// initialize #decisions array; one per participant
		this.decisions = new AtomicBoolean[4];	// TODO to replace 4
		
		
		// TODO Auto-generated method stub
		return false;
	}
}
