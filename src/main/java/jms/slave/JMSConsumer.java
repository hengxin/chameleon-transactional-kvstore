/**
 * 
 */
package jms.slave;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.naming.NamingException;

import jms.AbstractJMSParticipant;

/**
 * @author hengxin
 * @date 11-13-2015
 * 
 * <p> The consumers of the commit logs. They are the slaves.
 */
public class JMSConsumer extends AbstractJMSParticipant implements MessageListener
{
	private MessageConsumer consumer = null;
	
	/**
	 * Initialize the {@value #consumer} (an {@link MessageConsumer}).
	 * @throws NamingException May be thrown by super constructor.
	 * @throws JMSException Thrown when it fails to create an {@link MessageConsumer}, or
	 *  thrown by super constructor.
	 */
	public JMSConsumer() throws JMSException, NamingException
	{
		super();
		this.consumer = super.session.createConsumer(super.cl_topic);
		this.consumer.setMessageListener(this);
	}

	/* 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message msg)
	{
		
	}

}
