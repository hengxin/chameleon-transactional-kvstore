/**
 * 
 */
package jms;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
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
	// FIXME using command-line args or configuration file
	private static final String TOPIC = "CommitLogTopic";
	private static final String CONNECTION_FACTORY = "TopicCF";
	
	protected Topic cl_topic = null;
	private TopicConnection connection = null;
	protected TopicSession session = null;
	
	
	/**
	 * Initialization. Create a session.
	 * @throws NamingException May be thrown by {@link InitialContext}.
	 * @throws JMSException Thrown when it fails to create {@link Connection} or {@link Session}, or
	 *  fails to start a {@link Connection}.
	 */
	public AbstractJMSParticipant() throws JMSException, NamingException
	{
		Context ctx = new InitialContext();
		TopicConnectionFactory cf = (TopicConnectionFactory) ctx.lookup(AbstractJMSParticipant.CONNECTION_FACTORY);
		
		this.connection = cf.createTopicConnection();
		this.session = this.connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		
		this.cl_topic = (Topic) ctx.lookup(AbstractJMSParticipant.TOPIC);

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
