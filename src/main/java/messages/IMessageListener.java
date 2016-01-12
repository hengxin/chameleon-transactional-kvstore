package messages;

/**
 * 
 * @author hengxin
 * @date Created on Jan 12, 2016
 */
public interface IMessageListener {
	public abstract void onMessage(AbstractMessage msg);
	public abstract void bind(IMessageConsumer consumer);
}
