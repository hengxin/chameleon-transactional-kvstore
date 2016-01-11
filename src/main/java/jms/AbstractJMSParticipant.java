/**
 * 
 */
package jms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jms.master.JMSPublisher;
import jms.slave.JMSSubscriber;
import master.AbstractMaster;
import site.AbstractSite;

/**
 * {@link AbstractJMSParticipant} can be a {@link JMSPublisher} 
 * (such as an {@link AbstractMaster}) or a {@link JMSSubscriber}
 * (such as an {@link AbstractSite}).
 * @author hengxin
 * @date 11-13-2015
 */
public abstract class AbstractJMSParticipant {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractJMSParticipant.class);
	
	private static final String JMS_CONFIG_PROPERTIES_FILE = "jms/jms-config.properties";
	private static String TOPIC;
	private static String CONNECTION_FACTORY;

	protected Topic cl_topic = null;
	private TopicConnection connection = null;
	protected TopicSession session = null;

	protected TopicPublisher publisher = null;
	protected TopicSubscriber subscriber = null;
	
	/**
	 * Load JMS configuration, initialize context, create connection factory, create top connection,
	 * create session, create publisher or subscriber (as a message listener),
	 * and finally start the connection.
	 * 
	 * @throws NamingException
	 *             May be thrown by {@link InitialContext}.
	 * @throws JMSException
	 *             Thrown when it fails to create {@link Connection} or
	 *             {@link Session}, or fails to start a {@link Connection}.
	 */
	public AbstractJMSParticipant() {
		try {
			this.loadJMSConfig();
			
			Context ctx = new InitialContext();
			TopicConnectionFactory cf = (TopicConnectionFactory) ctx.lookup(AbstractJMSParticipant.CONNECTION_FACTORY);

			this.connection = cf.createTopicConnection();
			this.session = this.connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

			this.cl_topic = (Topic) ctx.lookup(AbstractJMSParticipant.TOPIC);

			this.participate();

			this.connection.start();
		} catch (NamingException ne) {
			LOGGER.error("The JNDI naming service for JMS fails.", ne.getCause());
			ne.printStackTrace();
			System.exit(1);
		} catch (JMSException jmse) {
			System.out.format("Fails to participate in JMS: %s.", jmse.getMessage());
			jmse.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Participate in JMS as a publisher (i.e., {@link JMSPublisher})
	 * or a subscriber (i.e., {@link JMSSubscriber}).
	 * @throws JMSException
	 */
	public abstract void participate() throws JMSException;

	/**
	 * Close the JMS connection.
	 * @throws JMSException
	 */
	public void close() throws JMSException {
		this.connection.close();
	}

	private void loadJMSConfig() {
		Properties prop = new Properties();
		
		ClassLoader class_loader = Thread.currentThread().getContextClassLoader();

		try (InputStream is = class_loader.getResourceAsStream(AbstractJMSParticipant.JMS_CONFIG_PROPERTIES_FILE);) {
			prop.load(is);
		
			AbstractJMSParticipant.CONNECTION_FACTORY = prop.getProperty("cf");	// FIXME hard-wired code here
			AbstractJMSParticipant.TOPIC = prop.getProperty("topic");
		} catch (IOException ioe) {
			LOGGER.error("Failed to load the JMS configuration file [{}]. \n", AbstractJMSParticipant.JMS_CONFIG_PROPERTIES_FILE, ioe.getCause());
		}
	}

}
