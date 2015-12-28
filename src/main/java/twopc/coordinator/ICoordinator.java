package twopc.coordinator;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;

/**
 * Interface for 2PC coordinator which coordinates the two-phase 
 * commit process among the masters involved.
 * <p>
 * Typically, one of the masters involved in the 2PC protocol 
 * plays the role of coordinator.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface ICoordinator
{
	/**
	 * Try to commit a transaction.
	 * @param tx	transaction to commit
	 * @param vcm 	version constraint manager for RVSI specification
	 * @return {@code true} if committed successfully; {@code false}, otherwise.
	 */
	public abstract boolean coorCommit2PC(ToCommitTransaction tx, VersionConstraintManager vcm);
}
