package messaging;

/**
 * @author hengxin
 * @date Created on Jan 12, 2016
 */
public interface IMessageListener {
	void accept(AbstractMessage msg);
	void register(IMessageConsumer consumer);
}
