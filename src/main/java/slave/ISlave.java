package slave;

import messages.AbstractMessage;

/**
 * @author hengxin
 * @date Created 10-28-2015
 * 
 * Interface for the slave sites
 */
public interface ISlave
{
	public abstract void onMessage(AbstractMessage message);
}
