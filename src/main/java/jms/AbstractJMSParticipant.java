/**
 * 
 */
package jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author hengxin
 * @date 11-13-2015
 * 
 * <p> A JMS participant: a publisher (i.e., the master) or a consumer (i.e., a slave)
 */
public abstract class AbstractJMSParticipant
{
	private static final String TOPIC = "/topic/CommitLogTopic";
	private static final String CONNECTION_FACTORY = "/ConnectionFactory";
	
	protected Topic cl_topic = null;
	private Connection connection = null;
	protected Session session = null;
	
	
	/**
	 * Initialization. Create a session.
	 * @throws NamingException May be thrown by {@link InitialContext}.
	 * @throws JMSException Thrown when it fails to create {@link Connection} or {@link Session}, or
	 *  fails to start a {@link Connection}.
	 */
	public AbstractJMSParticipant() throws JMSException, NamingException
	{
		InitialContext ic = new InitialContext();
		this.cl_topic = (Topic) ic.lookup(AbstractJMSParticipant.TOPIC);
		ConnectionFactory cf = (ConnectionFactory) ic.lookup(AbstractJMSParticipant.CONNECTION_FACTORY);
		
		this.connection = cf.createConnection();
		this.session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		this.connection.start();
	}
	
	/**
	 * Close the JMS connection.
	 * @throws JMSException
	 */
	public void close() throws JMSException
	{
		this.connection.close();
	}

}
