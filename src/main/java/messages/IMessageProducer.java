package messages;

import exception.transaction.TransactionCommunicationException;

/**
 * Role of a message producer, such as an {@link IMaster}.
 * 
 * @author hengxin
 * @date Created on 11-26-2015
 */
public interface IMessageProducer
{
	public abstract void send(AbstractMessage message) throws TransactionCommunicationException;
}
