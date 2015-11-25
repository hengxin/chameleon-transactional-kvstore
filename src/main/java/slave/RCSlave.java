package slave;

import kvs.table.AbstractTableHolder;
import kvs.table.SlaveTable;
import messages.AbstractMessage;

/**
 * @author hengxin
 * @date Created on 11-25-2015
 * 
 * A slave only need to enforce "Read Committed" isolation on {@link SlaveTable}.
 */
public class RCSlave extends AbstractTableHolder implements ISlave
{
	public RCSlave()
	{
		super.table = new SlaveTable();
	}

	@Override
	public void onMessage(AbstractMessage message)
	{
		
	}
}
