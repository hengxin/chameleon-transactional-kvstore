package slave.context;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.IContext;
import network.membership.AbstractStaticMembership;
import network.membership.Member;
import network.membership.SlaveMembership;

/**
 * Context for slave sites.
 * @author hengxin
 * @date Created on Dec 13, 2015
 */
public class SlaveContext implements IContext {

	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveContext.class);
	
	private AbstractStaticMembership slave_membership;

	/**
	 * Constructor using user-specified configuration file.
	 * @param file	path of .properties file; it cannot be {@code null}.
	 */
	public SlaveContext(@Nonnull String file) {
		LOGGER.info("Using the properties file ({}) for MasterContext.", file);
		this.slave_membership = new SlaveMembership(file);
	}

	@Override
	public @Nonnull Member self() {
		return this.slave_membership.self();
	}

}
