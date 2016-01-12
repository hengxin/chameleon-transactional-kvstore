package jms.master;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.jms.JMSException;

import exception.transaction.TransactionCommunicationException;
import jms.AbstractJMSParticipant;
import messages.AbstractMessage;
import messages.IMessageProducer;

/**
 * {@link JMSPublisher} serves as a message publisher in JMS.
 * @author hengxin
 * @date Created on 11-12-2015
 */
public final class JMSPublisher extends AbstractJMSParticipant implements IMessageProducer {
	
	private final ExecutorService exec = Executors.newCachedThreadPool(); 

	/**
	 * Send out an {@link AbstractMessage}.
	 * @param msg an {@link AbstractMessage} to publish
	 * @throws TransactionCommunicationException
     * @implNote
     * 	FIXME using #createByteMessage() instead of #createObjectMessage() 
     * 		for better performance and portability.
	 */
	public void send(AbstractMessage msg) {
//		try {
//			super.publisher.publish(super.session.createObjectMessage(msg));
//		} catch (JMSException jmse) {
//			LOGGER.warn("Cannot publish this message [{}]. \\n [{}]", msg, jmse);
//		}
		
		Future<Void> dummy_future = this.exec.submit(() -> {
			super.publisher.publish(super.session.createObjectMessage(msg));
			return null;
		});
		
		try {
			dummy_future.get();	// FIXME To test if this is necessary???
		} catch (InterruptedException | ExecutionException e) {
			throw new TransactionCommunicationException(String.format("Failded to publish the commit log [{}].", msg), e);	
		}
	}

	/**
	 * Participate as a publisher.
	 */
	@Override
	public void participate() throws JMSException {
		super.publisher = super.session.createPublisher(super.cl_topic);
	}

}
