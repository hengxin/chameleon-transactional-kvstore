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
	 * @param msg An {@link AbstractMessage} to publish
     * @throws JMSException  if the JMS provider fails to publish the message due to some internal error
	 */
	public void publish(AbstractMessage msg) throws JMSException
	{
		/**
		 * FIXME using #createByteMessage() instead of #createObjectMessage() 
		 * for good performance and portability.
		 * 
		 * <p> not necessary??? {@link AbstractMessage} implements {@link Serializable}
		 */
		this.publisher.publish(super.session.createObjectMessage(msg));
	}
}
