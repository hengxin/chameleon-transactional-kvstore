/**
 * 
 */
package jms.slave;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import jms.AbstractJMSParticipant;
import jms.master.JMSCommitLogPublisher;
import kvs.table.AbstractSite;
import messages.AbstractMessage;
import slave.RCSlave;

/**
 * <p>
 * The subscribers of the commit logs. They are the slaves.
 * 
 * @author hengxin
 * @date 11-13-2015
 */
public class JMSCommitLogSubscriber extends AbstractJMSParticipant implements MessageListener
{
	/**
	 * Receive messages from {@link JMSCommitLogPublisher},
	 * and dispatch them to {@link #site} (which should be a {@link RCSlave}).
	 */
	@Override
	public void onMessage(Message msg)
	{
		ObjectMessage obj_msg = (ObjectMessage) msg;
		try
		{
			AbstractMessage a_msg = (AbstractMessage) obj_msg.getObject();
			((RCSlave) super.site).onMessage(a_msg);
		} catch (JMSException jmse)
		{
			System.out.format("Fail to receive messages, due to %s.", jmse.getMessage());
			jmse.printStackTrace();
		}
	}

	/**
	 * Participate as a subscriber.
	 * <p>
	 * FIXME unsafe object publishing???
	 */
	@Override
	public void participate() throws JMSException
	{
		super.subscriber = super.session.createSubscriber(super.cl_topic);
		super.subscriber.setMessageListener(this);
	}

	@Override
	public void bindto(AbstractSite site)
	{
		super.site = site;
	}

}
