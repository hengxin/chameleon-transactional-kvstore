package slave;

import jms.AbstractJMSParticipant;
import kvs.table.AbstractSite;
import kvs.table.SlaveTable;
import messages.AbstractMessage;
import messages.IMessageConsumer;

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
	public void onMessage(AbstractMessage message)
	{
		
	}
	
	@Override
	public void registerAsJMSParticipant(AbstractJMSParticipant jmser)
	{
		super.registerAsJMSParticipant(jmser);
		jmser.bindto(this);
	}
}
