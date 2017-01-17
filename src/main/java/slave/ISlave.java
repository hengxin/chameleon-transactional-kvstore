package slave;

import client.clientlibrary.transaction.ToCommitTransaction;

/**
 * Interface for the slave sites.
 * 
 * @author hengxin
 * @date Created 10-28-2015
 * 
 * @implNote
 * 		The {@link #apply(ToCommitTransaction)} method will not be remotely invoked.
 * 		Thus {@link ISlave} does not extend {@link Remote}.
 */
@Deprecated
public interface ISlave {
	public void apply(ToCommitTransaction tx);
}
