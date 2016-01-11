package messages;

/**
 * Interface {@link IMessageConsumer} exposes an 
 * {@link #onMessage(AbstractMessage)} operation for its implementation
 * classes to consume an {@link AbstractMessage}.
 * @author hengxin
 * @date Created on 11-26-2015
 * @see {@link IMessageProducer}
 */
public interface IMessageConsumer {
	public abstract void onMessage(AbstractMessage msg);
}
