package master.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import context.AbstractContext;
import membership.site.StaticSiteMembershipFromProperties;

/**
 * Providing context for master sites, including
 * <p>
 * <ul>
 * <li> {@link #membership} membership info. for this master
 * </ul>
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class MasterContext extends AbstractContext { // implements IContext {
	private final static Logger LOGGER = LoggerFactory.getLogger(MasterContext.class);
	
	/**
	 * Constructor using user-specified properties file.
	 * @param file path of the properties file; it cannot be {@code null}.
	 */
	public MasterContext(@Nonnull String file) {
		LOGGER.info("Using the properties file [{}] for [{}].", file, this.getClass().getSimpleName());
        membership = new StaticSiteMembershipFromProperties(file);
	}

}
