package master.twopc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import context.IContext;
import jms.master.JMSPublisher;
import kvs.table.MasterTable;
import master.AbstractMaster;
import twopc.participant.I2PCParticipant;

/**
 * {@link MasterIn2PC} represents a master site involved in 
 * the 2PC protocol for distributed transactions.
 * @author hengxin
 * @date Created on Jan 9, 2016
 */
public final class MasterIn2PC extends AbstractMaster implements I2PCParticipant {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MasterIn2PC.class);
	
	/**
	 * Constructor with {@link MasterTable} as the default underlying table
	 * and {@link JMSPublisher} as the default mechanism of message propagation.
	 * @param context	context for this master
	 */
	public MasterIn2PC(IContext context) {
		super(context, new JMSPublisher());
	}

	/**
	 * Constructor with {@link MasterTable} as the default underlying table
	 * and user-specified mechanism of message propagation.
	 * @param context
	 * @param jms_publisher
	 */
	public MasterIn2PC(IContext context, JMSPublisher jms_publisher) {
		super(context, jms_publisher);
	}

	@Override
	public boolean prepare(ToCommitTransaction tx, VersionConstraintManager vcm) {
		// TODO Auto-generated method stub
        LOGGER.debug("Master [{}] receives ToCommitTransaction [{}] and associated VersionConstraintManager [{}] " +
                "at the PREPARE phase", toString(), tx.toString(), vcm.toString());
		return false;
	}

	@Override
	public boolean complete() {
		// TODO Auto-generated method stub
        LOGGER.debug("Master [{}] at the COMMIT phase.", toString());
		return false;
	}

}
