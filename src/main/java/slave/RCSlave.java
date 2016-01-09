package slave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.transaction.ToCommitTransaction;
import context.IContext;
import jms.AbstractJMSParticipant;
import kvs.table.SlaveTable;
import messages.AbstractMessage;
import messages.IMessageConsumer;
import site.AbstractSite;

/**
 * A {@link RCSlave} only needs to enforce the "Read Committed" isolation
 * on the underlying {@link super#table}.
 * <p>Note that in the architecture, slave sites are not involved in the
 * distributed transaction protocol. Therefore, {@link RCSlave} does not
 * implement interface {@link ITransactional}.
 * 
 * @author hengxin
 * @date Created on 11-25-2015
 */
public final class RCSlave extends AbstractSite implements IMessageConsumer {

	private final static Logger LOGGER = LoggerFactory.getLogger(RCSlave.class);
	
	public RCSlave(IContext context) {
		super(context);
		super.table = new SlaveTable();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessage(AbstractMessage msg) {
		LOGGER.info("Receiving commit log [{}].", msg);
		super.table.apply((ToCommitTransaction) msg);
	}
	
	@Override
	public void registerAsJMSParticipant(AbstractJMSParticipant jmser) {
		super.registerAsJMSParticipant(jmser);
		jmser.bindto(this);
	}

}
