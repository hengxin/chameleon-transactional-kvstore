package master.twopc;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.RemoteException;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import exception.transaction.TransactionExecutionException;
import kvs.component.Timestamp;
import twopc.PreparedResult;
import twopc.participant.I2PCParticipant;

/**
 * {@link MasterIn2PC} represents a master site involved in 
 * the 2PC protocol for distributed transactions.
 * @author hengxin
 * @date Created on Jan 9, 2016
 */
@Deprecated
public final class MasterIn2PC implements I2PCParticipant {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MasterIn2PC.class);
	
	@Override
	public PreparedResult prepare(@NotNull ToCommitTransaction tx, @NotNull VersionConstraintManager vcm) {
		// TODO Auto-generated method stub
        LOGGER.debug("Master [{}] receives ToCommitTransaction [{}] and associated VersionConstraintManager [{}] " +
                "at the PREPARE phase", toString(), tx.toString(), vcm.toString());
		return null;
	}

    @Override
    public boolean commit(ToCommitTransaction tx, Timestamp cts) throws RemoteException, TransactionExecutionException {
        LOGGER.debug("Master [{}] at the COMMIT phase.", toString());
        return false;
    }

    @Override
    public void abort(ToCommitTransaction tx) {

    }

}
