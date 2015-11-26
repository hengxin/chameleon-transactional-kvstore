package messages;

/**
 * @author hengxin
 * @date Created on 11-26-2015
 * 
 * Role of a message consumer, such as an {@link ISlave}.
 */
public interface IMessageConsumer
{
	public abstract void onMessage(AbstractMessage message);
}
