package jms.master;

import javax.jms.JMSException;
import javax.jms.TopicPublisher;

import jms.AbstractJMSParticipant;
import messages.AbstractMessage;

/**
 * @author hengxin
 * @date Created on 11-12-2015
 * 
 * <p> The publisher of the commit logs. It is the master.
 */
public class JMSCommitLogPublisher extends AbstractJMSParticipant
{
	private TopicPublisher publisher = null;
	
	/**
	 * @param msg An {@link AbstractMessage} to publish
     * @throws JMSException  if the JMS provider fails to publish the message due to some internal error
	 */
	public void publish(AbstractMessage msg) throws JMSException
	{
		/**
		 * FIXME using #createByteMessage() instead of #createObjectMessage() 
		 * for better performance and portability.
		 * 
		 * <p> not necessary??? {@link AbstractMessage} implements {@link Serializable}
		 */
		this.publisher.publish(super.session.createObjectMessage(msg));
	}

	/**
	 * Participate as a publisher.
	 */
	@Override
	public void participate() throws JMSException
	{
		this.publisher = super.session.createPublisher(super.cl_topic);
	}
}
