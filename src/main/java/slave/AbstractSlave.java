package slave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

import client.clientlibrary.transaction.ToCommitTransaction;
import context.AbstractContext;
import messaging.AbstractMessage;
import messaging.IMessageConsumer;
import messaging.IMessageListener;
import messaging.IMessageListener2;
import site.AbstractSite;

/**
 * {@link AbstractSlave} extends the basic functionalities of {@link AbstractSite}
 * by implementing {@link IMessageConsumer}; therefore, an {@link AbstractSlave} is able
 * to receive {@link AbstractMessage} and handle with it.
 * @author hengxin
 * @date Created on Jan 10, 2016
 */
public abstract class AbstractSlave extends AbstractSite implements IMessageConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSlave.class);
    private static ExecutorService exec = Executors.newCachedThreadPool();

    /**
     * Constructor with {@link AbstractContext} and possible {@link IMessageListener2}.
     *
     * @param context context for this slave site
     * @param listener2  the underlying mechanism for accepting messages;
     *                   it may be {@code null}.
     */
    public AbstractSlave(AbstractContext context, @Nullable IMessageListener2 listener2) {
        super(context);
        if (listener2 != null) {
            listener2.register(this);
            exec.submit(listener2::accept);
        }
    }

	/**
	 * @param context	context for this slave site
	 * @param listener	the underlying mechanism of receiving messages; 
	 * 	it can be {@code null} if this slave site does not receive messages. 
	 */
	@Deprecated
	public AbstractSlave(AbstractContext context, @Nullable IMessageListener listener) {
		super(context);
		if (listener != null)
			listener.register(this);
	}

	@Override
	public void consume(AbstractMessage msg) {
		LOGGER.info("Receiving commit log [{}].", msg);
		super.table.apply((ToCommitTransaction) msg);
	}

}
