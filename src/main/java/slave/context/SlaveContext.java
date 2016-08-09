package slave.context;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.AbstractContext;
import network.membership.StaticMembershipFromProperties;

/**
 * Context for slave sites.
 * @author hengxin
 * @date Created on Dec 13, 2015
 */
public class SlaveContext extends AbstractContext {
	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveContext.class);

	/**
	 * Constructor using user-specified configuration file.
	 * @param file	path of .properties file; it cannot be {@code null}.
	 */
	public SlaveContext(@NotNull String file) {
		LOGGER.info("Using the properties file [{}] for [{}].", file, this.getClass().getSimpleName());
		membership = new StaticMembershipFromProperties(file);
	}

}
