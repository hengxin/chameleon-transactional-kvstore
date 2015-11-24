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
 *       <p>
 *       A JMS participant: a publisher (i.e., the master) or a consumer (i.e.,
 *       a slave)
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
	 * Initialize context, create connection factory, create top connection,
	 * create session, create publisher or subscriber (as a message listener),
	 * and finally start the connection.
	 * 
	 * @throws NamingException
	 *             May be thrown by {@link InitialContext}.
	 * @throws JMSException
	 *             Thrown when it fails to create {@link Connection} or
	 *             {@link Session}, or fails to start a {@link Connection}.
	 */
	public AbstractJMSParticipant()
	{
		try
		{
			Context ctx = new InitialContext();
			TopicConnectionFactory cf = (TopicConnectionFactory) ctx.lookup(AbstractJMSParticipant.CONNECTION_FACTORY);

			this.connection = cf.createTopicConnection();
			this.session = this.connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

			this.cl_topic = (Topic) ctx.lookup(AbstractJMSParticipant.TOPIC);

			this.participate();

			this.connection.start();
		} catch (NamingException ne)
		{
			System.out.format("The JNDI naming service for JMS fails: %s", ne.getMessage());
			ne.printStackTrace();
			System.exit(1);
			
		} catch (JMSException jmse)
		{
			System.out.format("Fails to participate in JMS: %s", jmse.getMessage());
			jmse.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Participate in JMS as a publisher (i.e., {@link JMSCommitLogPublisher})
	 * or a subscriber (i.e., {@link JMSCommitLogSubscriber}).
	 * 
	 * @throws JMSException
	 */
	public abstract void participate() throws JMSException;

	/**
	 * Close the JMS connection.
	 * 
	 * @throws JMSException
	 */
	public void close() throws JMSException
	{
		this.connection.close();
	}

}
