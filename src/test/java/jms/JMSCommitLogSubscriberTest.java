package jms;

import static org.junit.Assert.*;

import org.codehaus.groovy.control.messages.SimpleMessage;
import org.junit.Before;
import org.junit.Test;

import jms.master.JMSCommitLogPublisher;
import jms.slave.JMSCommitLogSubscriber;
import messages.AbstractMessage;

public class JMSCommitLogSubscriberTest
{
	private final AbstractJMSParticipant publisher = new JMSCommitLogPublisher();
	private final AbstractJMSParticipant subscriber = new JMSCommitLogSubscriber();
	
	@Before
	public void setUp() throws Exception
	{
		((JMSCommitLogPublisher) this.publisher).publish(new SimpleMessage("Hello JMS."));
	}

	@Test
	public void testParticipate()
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testOnMessage()
	{
		
	}
	
	private final class SimpleMessage extends AbstractMessage
	{
		private final String msg;
		
		public SimpleMessage(String msg)
		{
			this.msg = msg;
		}

		public String getMessage()
		{
			return this.msg;
		}
	}

}
