package master;

import com.sun.istack.Nullable;

import java.util.Optional;

import context.AbstractContext;
import messages.IMessageConsumer;
import messages.IMessageProducer;
import site.AbstractSite;

/**
 * {@link AbstractMaster} extends the basic functionalities of {@link AbstractSite}
 * by holding an {@link IMessageProducer};
 * therefore, an {@link AbstractMaster} is able to send (or, publish) messages to {@link IMessageConsumer}s.
 * @author hengxin
 * @date Created on Jan 9, 2016
 */
public abstract class AbstractMaster extends AbstractSite {

	protected final Optional<IMessageProducer> messenger;

	/**
	 * @param context	context for the master site
	 * @param messenger		the underlying mechanism of message propagation; 
	 * 	it can be {@code null} if this master site does not need to propagate messages. 
	 */
	public AbstractMaster(AbstractContext context, @Nullable IMessageProducer messenger) {
		super(context);
		this.messenger = Optional.of(messenger);
	}

}
