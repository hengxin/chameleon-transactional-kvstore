package slave;

import client.clientlibrary.transaction.ToCommitTransaction;

/**
 * Interface for the slave sites.
 * 
 * @author hengxin
 * @date Created 10-28-2015
 * 
 * @note
 * 		The {@link #apply(ToCommitTransaction)} method will not be remotely invoked.
 * 		Thus {@link ISlave} does not extend {@link Remote}.
 */
public interface ISlave
{
	public abstract void apply(ToCommitTransaction tx);
}
