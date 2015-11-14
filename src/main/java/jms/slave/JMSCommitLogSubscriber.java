/**
 * 
 */
package jms.slave;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TopicSubscriber;
import javax.naming.NamingException;

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
	
	/**
	 * Initialize the {@value #subscriber} (an {@link TopicSubscriber}).
	 * @throws NamingException May be thrown by super constructor.
	 * @throws JMSException Thrown when it fails to create an {@link TopicSubscriber}, or
	 *  thrown by super constructor.
	 */
	public JMSCommitLogSubscriber() throws JMSException, NamingException
	{
		super();
		this.subscriber = super.session.createSubscriber(super.cl_topic);
		this.subscriber.setMessageListener(this);
	}

	/* 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message msg)
	{
		
	}

}
