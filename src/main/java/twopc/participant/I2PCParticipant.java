package twopc.participant;

import java.rmi.Remote;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;

/**
 * Interface {@link I2PCParticipant} exposes 
 * {@link #prepare(ToCommitTransaction, VersionConstraintManager)}
 * and {@link #complete()} for participants of 2PC protocol.
 * These operations are available remotely, by letting this interface
 * extend {@link Remote}.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface I2PCParticipant extends Remote {

	public abstract boolean prepare(ToCommitTransaction tx, VersionConstraintManager vcm);
	public abstract boolean complete();	// FIXME parameters

}
