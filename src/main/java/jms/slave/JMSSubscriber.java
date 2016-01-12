/**
 * 
 */
package jms.slave;

import javax.annotation.Nonnull;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jms.AbstractJMSParticipant;
import jms.master.JMSPublisher;
import messages.AbstractMessage;
import messages.IMessageConsumer;
import messages.IMessageListener;

/**
 * {@link JMSSubscriber} serves as a subscriber of messages in JMS. 
 * It implements the {@link IMessageListener} interface.
 * Note that it also implements an interface called {@link MessageListener},
 * which is necessary for JMS communication.
 * @author hengxin
 * @date 11-13-2015
 * @see	{@link JMSPublisher}
 */
public class JMSSubscriber extends AbstractJMSParticipant implements MessageListener, IMessageListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JMSSubscriber.class);

	@Nonnull private IMessageConsumer consumer;

	/**
	 * Overridden method from {@link MessageListener} required by JMS:
	 * Receives {@link Message} from {@link JMSPublisher},
	 * casts it into application-specific {@link AbstractMessage},
	 * and then directs it to the application-specific
	 * {@link IMessageListener#onMessage(AbstractMessage)}.
	 */
	@Override
	public void onMessage(Message msg) {
		ObjectMessage obj_msg = (ObjectMessage) msg;
		try {
			AbstractMessage a_msg = (AbstractMessage) obj_msg.getObject();
			this.onMessage(a_msg);
		} catch (JMSException jmse) {
			LOGGER.warn("Failed to handle messages. \\n [{}]", jmse);
		}
	}

	@Override
	public void onMessage(AbstractMessage msg) {
		this.consumer.consume(msg);
	}

	@Override
	public void bind(IMessageConsumer consumer) {
		this.consumer = consumer;
	}

	/**
	 * Participates as a subscriber.
	 */
	@Override
	public void participate() throws JMSException {
		super.subscriber = super.session.createSubscriber(super.cl_topic);
		super.subscriber.setMessageListener(this);	// FIXME unsafe object publishing???
	}

}
