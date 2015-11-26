package jms.master;

import javax.jms.JMSException;

import jms.AbstractJMSParticipant;
import kvs.table.AbstractSite;
import messages.AbstractMessage;

/**
 * <p> The publisher of the commit logs. It is the master.
 * 
 * @author hengxin
 * @date Created on 11-12-2015
 */
public class JMSCommitLogPublisher extends AbstractJMSParticipant
{
	/**
	 * @param msg An {@link AbstractMessage} to publish
     * @throws JMSException  if the JMS provider fails to publish the message due to some internal error.
	 */
	public void publish(AbstractMessage msg) throws JMSException
	{
		/**
		 * FIXME using #createByteMessage() instead of #createObjectMessage() 
		 * for better performance and portability.
		 */

		super.publisher.publish(super.session.createObjectMessage(msg));
	}

	/**
	 * Participate as a publisher.
	 */
	@Override
	public void participate() throws JMSException
	{
		super.publisher = super.session.createPublisher(super.cl_topic);
	}

	@Override
	public void bindto(AbstractSite site)
	{
	}
}
