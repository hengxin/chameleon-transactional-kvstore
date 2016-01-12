package messages;

import exception.transaction.TransactionCommunicationException;
import master.AbstractMaster;

/**
 * {@link IMessageProducer} represents a role of message producer,
 * and exposes {@link #send(AbstractMessage)} operation.
 * @author hengxin
 * @date Created on 11-26-2015
 * @see	{@link IMessageConsumer}
 * @see {@link AbstractMaster}
 */
public interface IMessageProducer {
	/**
	 * Sends a message to {@link IMessageConsumer}.
	 * @param msg	{@link AbstractMessage} to send
	 * @throws TransactionCommunicationException	if an error occurs during sending message 
	 */
	public abstract void send(AbstractMessage msg);
}
