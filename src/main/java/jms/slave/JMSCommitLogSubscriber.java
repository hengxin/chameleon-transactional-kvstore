/**
 * 
 */
package jms.slave;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TopicSubscriber;

import jms.AbstractJMSParticipant;

/**
 * @author hengxin
 * @date 11-13-2015
 * 
 * <p> The subscribers of the commit logs. They are the slaves.
 */
public class JMSCommitLogSubscriber extends AbstractJMSParticipant implements MessageListener
{
	private TopicSubscriber subscriber = null;
	
	@Override
	public void onMessage(Message msg)
	{
		
	}

	/**
	 * Participate as a subscriber.
	 */
	@Override
	public void participate() throws JMSException
	{
		this.subscriber = super.session.createSubscriber(super.cl_topic);
		this.subscriber.setMessageListener(this);
	}

}
