package master;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.istack.Nullable;

import context.IContext;
import exception.transaction.TransactionCommunicationException;
import jms.master.JMSPublisher;
import messages.AbstractMessage;
import messages.IMessageConsumer;
import messages.IMessageProducer;
import site.AbstractSite;

/**
 * {@link AbstractMaster} extends the basic functionalities of {@link AbstractSite}
 * by implementing {@link IMessageProducer}; therefore, an {@link AbstractMaster} is able
 * to send (or, publish) messages to {@link IMessageConsumer}s. 
 * @author hengxin
 * @date Created on Jan 9, 2016
 */
public abstract class AbstractMaster extends AbstractSite implements IMessageProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMaster.class);
	private final ExecutorService exec = Executors.newCachedThreadPool(); 
	
	/**
	 * @param context	context for the master site
	 * @param jms_publisher		the underlying mechanism of message propagation; 
	 * 	it can be {@code null} if this master site does not need to propagate messages. 
	 */
	public AbstractMaster(IContext context, @Nullable JMSPublisher jms_publisher) {
		super(context, jms_publisher);
	}

	@Override
	public void send(AbstractMessage msg) throws TransactionCommunicationException {
		Future<Void> dummy_future = this.exec.submit(() -> {
			((JMSPublisher) super.jmser.orElseThrow(() -> 
				new IllegalStateException(String.format("The master [%s] has not been registered as a JMS publisher.", this))))
				.publish(msg);

			LOGGER.info("The master [{}] has published the commit log [{}] to its slaves.", this, msg);
			return null;
		});
		
		try {
			dummy_future.get();	// FIXME To test if this is necessary???
		} catch (InterruptedException | ExecutionException e) {
			String log = String.format("I [{}] Failded to publish the commit log [{}]. \n {}", super.context.self(), msg);
			LOGGER.error(log);	// log at the master side
			throw new TransactionCommunicationException(log, e.getCause());	// thrown to the client side
		}
	}

}
