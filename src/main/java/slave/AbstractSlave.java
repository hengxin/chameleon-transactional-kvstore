package slave;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.transaction.ToCommitTransaction;
import context.IContext;
import jms.slave.JMSSubscriber;
import messages.AbstractMessage;
import messages.IMessageConsumer;
import messages.IMessageProducer;
import site.AbstractSite;

/**
 * {@link AbstractSlave} extends the basic functionalities of {@link AbstractSite}
 * by implementing {@link IMessageConsumer}; therefore, an {@link AbstractSlave} is able
 * to receive (or, subscribe to) messages from {@link IMessageProducer}. 
 * @author hengxin
 * @date Created on Jan 10, 2016
 */
public abstract class AbstractSlave extends AbstractSite implements IMessageConsumer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSlave.class);

	/**
	 * @param context	context for this slave site
	 * @param jms_subscriber	the underlying mechanism of receiving messages; 
	 * 	it can be {@code null} if this slave site does not receive messages. 
	 */
	public AbstractSlave(IContext context, @Nullable JMSSubscriber jms_subscriber) {
		super(context, jms_subscriber);
		jms_subscriber.bindto(this);
	}

	@Override
	public void onMessage(AbstractMessage msg) {
		LOGGER.info("Receiving commit log [{}].", msg);
		super.table.apply((ToCommitTransaction) msg);
	}

}
