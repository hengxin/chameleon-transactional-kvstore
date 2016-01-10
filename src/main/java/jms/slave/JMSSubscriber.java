/**
 * 
 */
package jms.slave;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import jms.AbstractJMSParticipant;
import jms.master.JMSPublisher;
import messages.AbstractMessage;
import site.AbstractSite;
import slave.AbstractSlave;

/**
 * {@link JMSSubscriber} serves as subscriber of messages in JMS. 
 * @author hengxin
 * @date 11-13-2015
 * @see	{@link JMSPublisher}
 */
public class JMSSubscriber extends AbstractJMSParticipant implements MessageListener {

	/**
	 * Receive messages from {@link JMSPublisher},
	 * and dispatch them to {@link #site} (which should be an {@link AbstractSlave}).
	 */
	@Override
	public void onMessage(Message msg) {
		ObjectMessage obj_msg = (ObjectMessage) msg;
		try {
			AbstractMessage a_msg = (AbstractMessage) obj_msg.getObject();
			((AbstractSlave) super.site).onMessage(a_msg);
		} catch (JMSException jmse) {
			System.out.format("Fail to receive messages, due to %s.", jmse.getMessage());
			jmse.printStackTrace();
		}
	}

	/**
	 * Participates as a subscriber.
	 */
	@Override
	public void participate() throws JMSException {
		super.subscriber = super.session.createSubscriber(super.cl_topic);
		super.subscriber.setMessageListener(this);	// FIXME unsafe object publishing???
	}

	public void bindto(AbstractSite site) {
		super.site = site;
	}

}
