package twopc.coordinator;

import java.rmi.Remote;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;

/**
 * Interface for 2PC coordinator which support 
 * both transaction start and commit methods.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface ICoordinator extends Remote
{
	/**
	 * @return start-timestamp of the transaction to start
	 */
	public abstract int cstart();
	
	/**
	 * Try to commit a transaction.
	 * @param tx	transaction to commit
	 * @param vcm 	version constraint manager for RVSI specification
	 * @return {@code true} if committed successfully; {@code false}, otherwise.
	 */
	public abstract boolean ccommit(ToCommitTransaction tx, VersionConstraintManager vcm);
}
