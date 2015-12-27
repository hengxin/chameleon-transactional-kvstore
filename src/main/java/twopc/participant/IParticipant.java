package twopc.participant;

import java.rmi.Remote;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;

/**
 * Interface for participants of 2PC protocol.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface IParticipant extends Remote
{
	public abstract boolean prepare2PC(ToCommitTransaction tx, VersionConstraintManager vcm);
	public abstract boolean commit2PC();	// FIXME parameters
}
