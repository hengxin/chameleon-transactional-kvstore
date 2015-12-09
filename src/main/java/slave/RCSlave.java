package slave;

import client.clientlibrary.transaction.ToCommitTransaction;
import jms.AbstractJMSParticipant;
import kvs.table.SlaveTable;
import messages.AbstractMessage;
import messages.IMessageConsumer;
import site.AbstractSite;

/**
 * A slave only need to enforce the "Read Committed" isolation on {@link SlaveTable}.
 * 
 * @author hengxin
 * @date Created on 11-25-2015
 */
public class RCSlave extends AbstractSite implements ISlave, IMessageConsumer
{
	public RCSlave()
	{
		super.table = new SlaveTable();
	}

	@Override
	public void onMessage(AbstractMessage a_msg)
	{
		super.table.apply((ToCommitTransaction) a_msg);
	}
	
	@Override
	public void registerAsJMSParticipant(AbstractJMSParticipant jmser)
	{
		super.registerAsJMSParticipant(jmser);
		jmser.bindto(this);
	}

	@Override
	public void apply(ToCommitTransaction tx)
	{
		super.table.apply(tx);
	}
}
