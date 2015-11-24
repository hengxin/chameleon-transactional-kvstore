package client.clientlibrary.transaction;

import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date Created on 11-23-2015
 * 
 * <p> To represent the transaction which is about to commit;
 * It consists of a transaction's start-timestamp and buffered updates.
 */
public class ToCommitTransaction
{
	private final Timestamp sts;
	private final BufferedUpdates buffered_Updates;
	
	public ToCommitTransaction(Timestamp sts, BufferedUpdates updates)
	{
		this.sts = sts;
		this.buffered_Updates = updates;
	}

	public Timestamp getSts()
	{
		return sts;
	}

	public BufferedUpdates getBuffered_Updates()
	{
		return buffered_Updates;
	}
}
