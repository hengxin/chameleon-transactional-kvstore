package twopc.coordinator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;

/**
 * Coordinator of 2PC protocol.
 * <p>
 * The coordinator executes an optimized 2PC protocol
 * with "early commit notification".
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public final class Coordinator implements ICoordinator
{
	private final ExecutorService exec = Executors.newCachedThreadPool();
	
	@Override
	public boolean coorCommit2PC(ToCommitTransaction tx, VersionConstraintManager vcm)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
