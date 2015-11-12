package master.communication.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import messages.AbstractMessage;

/**
 * The producer/sender of commit logs
 * @author hengxin
 * @date Created on 11-12-2015
 */
public class JMSProducer
{
	private Session session = null;
	private MessageProducer producer = null;
	
	/**
	 * Initialize the {@value #producer} (an {@link MessageProducer}).
	 * @throws NamingException May be thrown by {@link InitialContext}.
	 * @throws JMSException Thrown when it fails to create {@link Connection}, {@link Session}, or {@link MessageProducer}, or
	 *  fails to start a {@link Connection}.
	 */
	public JMSProducer() throws NamingException, JMSException
	{
		InitialContext ic = new InitialContext();
		Topic cl_topic = (Topic) ic.lookup("/topic/CommitLogTopic");
		ConnectionFactory cf = (ConnectionFactory) ic.lookup("/ConnectionFactory");
		
		Connection connection = cf.createConnection();
		this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		this.producer = session.createProducer(cl_topic);
		
		connection.start();
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
		this.producer.send(this.session.createObjectMessage(msg));
	}
}
