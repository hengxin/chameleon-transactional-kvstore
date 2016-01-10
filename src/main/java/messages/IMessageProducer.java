package messages;

import exception.transaction.TransactionCommunicationException;

/**
 * {@link IMessageProducer} represents a role of message producer,
 * and exposes {@link #send(AbstractMessage)} operation.
 * <p> In this project, {@link AbstractMaster} implements this interface
 * to send messages to {@link RCSlave}s, which implements {@link IMessageConsumer}.
 * @author hengxin
 * @date Created on 11-26-2015
 * @see	{@link IMessageConsumer}
 * @see {@link AbstractMaster}
 */
public interface IMessageProducer {
	/**
	 * Sends a message to {@link IMessageConsumer}.
	 * @param message	{@link AbstractMessage} to send
	 * @throws TransactionCommunicationException	if an error occurs during sending message 
	 */
	public abstract void send(AbstractMessage message) throws TransactionCommunicationException;
}
