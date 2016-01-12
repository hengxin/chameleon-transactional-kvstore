package messages;

/**
 * Interface {@link IMessageConsumer} exposes an 
 * {@link #consume(AbstractMessage)} operation for its implementation
 * classes to consume an {@link AbstractMessage}.
 * @author hengxin
 * @date Created on 11-26-2015
 * @see {@link IMessageProducer}
 */
public interface IMessageConsumer {
	/**
	 * Handles with @param msg.
	 * @param msg	{@link AbstractMessage} to consume.
	 */
	public abstract void consume(AbstractMessage msg);
}
