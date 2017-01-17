package slave.context;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.AbstractContext;
import membership.site.StaticSiteMembershipFromProperties;

/**
 * Context for slave sites.
 * @author hengxin
 * @date Created on Dec 13, 2015
 */
public class SlaveContext extends AbstractContext {
	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveContext.class);

	/**
	 * Constructor using user-specified configuration file.
	 * @param siteProperties  path of site.properties file; it cannot be {@code null}
	 */
	public SlaveContext(@NotNull String siteProperties) {
        LOGGER.info("Using the properties file [{}] for [{}].", siteProperties, this.getClass().getSimpleName());
		membership = new StaticSiteMembershipFromProperties(siteProperties);
	}

}
