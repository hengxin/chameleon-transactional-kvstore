package jms.master;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.naming.NamingException;

import jms.AbstractJMSParticipant;
import messages.AbstractMessage;

/**
 * @author hengxin
 * @date Created on 11-12-2015
 * 
 * <p> The producer of the commit logs. It is the master.
 */
public class JMSProducer extends AbstractJMSParticipant
{
	private MessageProducer producer = null;
	
	/**
	 * Initialize the {@value #producer} (an {@link MessageProducer}).
	 * @throws NamingException May be thrown by super constructor.
	 * @throws JMSException Thrown when it fails to create an {@link MessageProducer}, or
	 *  thrown by super constructor.
	 */
	public JMSProducer() throws NamingException, JMSException
	{
		super();
		this.producer = super.session.createProducer(super.cl_topic);
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
		this.producer.send(super.session.createObjectMessage(msg));
	}
}
