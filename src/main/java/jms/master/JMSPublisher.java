package jms.master;

import javax.jms.JMSException;

import jms.AbstractJMSParticipant;
import messages.AbstractMessage;

/**
 * {@link JMSPublisher} serves as a message publisher in JMS.
 * @author hengxin
 * @date Created on 11-12-2015
 */
public final class JMSPublisher extends AbstractJMSParticipant {

	/**
	 * @param msg an {@link AbstractMessage} to publish
     * @throws JMSException  if the JMS provider fails to publish the message due to some internal error
     * @implNote
     * 	FIXME using #createByteMessage() instead of #createObjectMessage() 
     * 		for better performance and portability.
	 */
	public void publish(AbstractMessage msg) throws JMSException {
		super.publisher.publish(super.session.createObjectMessage(msg));
	}

	/**
	 * Participate as a publisher.
	 */
	@Override
	public void participate() throws JMSException {
		super.publisher = super.session.createPublisher(super.cl_topic);
	}

}
