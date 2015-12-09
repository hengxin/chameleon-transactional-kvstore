package messages;

/**
 * @author hengxin
 * @date Created on 11-26-2015
 */
public interface IMessageConsumer
{
	public abstract void onMessage(AbstractMessage message);
}
