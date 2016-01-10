package slave;

import com.sun.istack.Nullable;

import context.IContext;
import jms.slave.JMSSubscriber;
import kvs.table.SlaveTable;
import messages.IMessageConsumer;
import site.ITransactional;

/**
 * A {@link RCSlave} only needs to enforce the "Read Committed" isolation
 * on the underlying {@link super#table}.
 * <p>Note that in the architecture, slave sites are not involved in the
 * distributed transaction protocol. Therefore, {@link RCSlave} does not
 * implement interface {@link ITransactional}.
 * 
 * @author hengxin
 * @date Created on 11-25-2015
 */
public final class RCSlave extends AbstractSlave implements IMessageConsumer {

	/**
	 * Constructor with {@link SlaveTable} as the default underlying table
	 * and with {@link JMSSubscriber} as the default underlying 
	 * mechanism of receiving message.
	 * @param context	context for this slave site
	 */
	public RCSlave(IContext context) {
		super(context, new JMSSubscriber());
		super.table = new SlaveTable();
	}
	
	/**
	 * Constructor with {@link SlaveTable} as the default underlying table
	 * and with user-specified {@link JMSSubscriber} as the underlying
	 * mechanism of message propagation.
	 * @param context	context for the master site
	 * @param jms_subscriber	the underlying mechanism of receiving messages; 
	 * 	it can be {@code null} if this slave site does not receive messages. 
	 * @implNote
	 *   FIXME removing the default {@link SlaveTable}; putting it into the parameters.
	 */
	public RCSlave(IContext context, @Nullable JMSSubscriber jms_subscriber) {
		super(context, jms_subscriber);
	}

}
