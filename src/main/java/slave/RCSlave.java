package slave;

import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import context.IContext;
import exception.transaction.TransactionExecutionException;
import jms.AbstractJMSParticipant;
import kvs.component.Timestamp;
import kvs.table.SlaveTable;
import messages.AbstractMessage;
import messages.IMessageConsumer;
import site.AbstractSite;

/**
 * A slave only need to enforce the "Read Committed" isolation on {@link SlaveTable}.
 * 
 * @author hengxin
 * @date Created on 11-25-2015
 */
public class RCSlave extends AbstractSite implements IMessageConsumer
{
	private final static Logger LOGGER = LoggerFactory.getLogger(RCSlave.class);
	
	public RCSlave(IContext context)
	{
		super(context);
		super.table = new SlaveTable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessage(AbstractMessage msg)
	{
		LOGGER.info("Receiving commit log [{}].", msg);
		super.table.apply((ToCommitTransaction) msg);
	}
	
	@Override
	public void registerAsJMSParticipant(AbstractJMSParticipant jmser)
	{
		super.registerAsJMSParticipant(jmser);
		jmser.bindto(this);
	}

	@Override
	public Timestamp start() throws RemoteException, TransactionExecutionException
	{
		throw new UnsupportedOperationException("Slaves do not support transaction start.");
	}

	@Override
	public boolean commit(ToCommitTransaction tx, VersionConstraintManager vc_manager) throws RemoteException
	{
		throw new UnsupportedOperationException("Slaves do not support transaction commit.");
	}
}
