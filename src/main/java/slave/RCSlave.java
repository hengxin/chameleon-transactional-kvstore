package slave;

import org.jetbrains.annotations.Nullable;

import context.AbstractContext;
import kvs.table.SlaveTable;
import messaging.IMessageListener;
import messaging.IMessageListener2;
import messaging.jms.slave.JMSSubscriber;
import site.ITransactional;

/**
 * A {@link RCSlave} only needs to enforce the "Read Committed" isolation
 * on the underlying {@link super#table}.
 * <p>Note that in the architecture, slave sites are not involved in the
 * distributed transaction protocol. Therefore, {@link RCSlave} does not
 * implement interface {@link ITransactional}.
 * @author hengxin
 * @date Created on 11-25-2015
 */
public final class RCSlave extends AbstractSlave {

	/**
	 * Constructor with {@link SlaveTable} as the default underlying table
	 * and with {@link JMSSubscriber} as the default underlying 
	 * mechanism for receiving message.
	 * @param context	context for this slave site
	 */
	public RCSlave(AbstractContext context) {
		super(context, new JMSSubscriber());
		table = new SlaveTable();
	}

	public RCSlave(AbstractContext context, @Nullable IMessageListener2 listener2) {
	    super(context, listener2);
        table = new SlaveTable();
    }

	/**
	 * Constructor with {@link SlaveTable} as the default underlying table
	 * and with user-specified {@link IMessageListener} as the underlying
	 * mechanism of message communication.
	 * @param context	context for the master site
	 * @param listener	the underlying mechanism of receiving messages; 
	 * 	it can be {@code null} if this slave site does not receive messages. 
	 * @implNote
	 *   FIXME removing the default {@link SlaveTable}; putting it into the parameters.
	 */
	@Deprecated
	public RCSlave(AbstractContext context, @Nullable IMessageListener listener) {
		super(context, listener);
	}

}
