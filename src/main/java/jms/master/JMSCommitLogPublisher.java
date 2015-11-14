package jms.master;

import javax.jms.JMSException;
import javax.jms.TopicPublisher;
import javax.naming.NamingException;

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
	 * Initialize the {@value #publisher} (an {@link TopicPublisher}).
	 * @throws NamingException May be thrown by super constructor.
	 * @throws JMSException Thrown when it fails to create an {@link TopicPublisher}, or
	 *  thrown by super constructor.
	 */
	public JMSCommitLogPublisher() throws NamingException, JMSException
	{
		super();
		this.publisher = super.session.createPublisher(super.cl_topic);
	}
	
	/**
	 * Send (i.e., push) messages.
	 * @param msg An {@link AbstractMessage} to send
	 * @throws JMSException
	 */
	public void send(AbstractMessage msg) throws JMSException
	{
		// FIXME using createByteMessage instead of createObjectMessage for good performance
		// not necessary??? {@link AbstractMessage} implements {@link Serializable}
//		session.createBytesMessage().
		this.publisher.publish(super.session.createObjectMessage(msg));
	}
}
