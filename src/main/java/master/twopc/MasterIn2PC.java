package master.twopc;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import twopc.participant.I2PCParticipant;

/**
 * {@link MasterIn2PC} represents a master site involved in 
 * the 2PC protocol for distributed transactions.
 * @author hengxin
 * @date Created on Jan 9, 2016
 */
public class MasterIn2PC implements I2PCParticipant {

	@Override
	public boolean prepare(ToCommitTransaction tx, VersionConstraintManager vcm) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean complete() {
		// TODO Auto-generated method stub
		return false;
	}

}
