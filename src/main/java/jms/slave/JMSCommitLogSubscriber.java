/**
 * 
 */
package jms.slave;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import jms.AbstractJMSParticipant;
import kvs.table.AbstractSite;

/**
 * <p> The subscribers of the commit logs. They are the slaves.
 * 
 * @author hengxin
 * @date 11-13-2015
 */
public class JMSCommitLogSubscriber extends AbstractJMSParticipant implements MessageListener
{
	@Override
	public void onMessage(Message msg)
	{
		
	}

	/**
	 * Participate as a subscriber.
	 * <p> FIXME unsafe object publishing???
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
