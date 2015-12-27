package twopc.coordinator;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import twopc.timing.ITimestampOracle;

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
	private final ITimestampOracle ts_oracle;
	
	public Coordinator(ITimestampOracle ts_oracle)
	{
		this.ts_oracle = ts_oracle;
	}

	@Override
	public int cstart()
	{
		return ts_oracle.get();
	}

	@Override
	public boolean ccommit(ToCommitTransaction tx, VersionConstraintManager vcm)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
}
