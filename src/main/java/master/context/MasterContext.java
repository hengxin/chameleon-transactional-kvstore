package master.context;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.IContext;
import network.membership.AbstractStaticMembership;
import network.membership.MasterMembership;
import network.membership.Member;

/**
 * Providing context for master sites, including
 * <p>
 * <ul>
 * <li> {@link #slave_stubs}: a list of its slaves (not used yet)
 * </ul>
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class MasterContext implements IContext {

	private final static Logger LOGGER = LoggerFactory.getLogger(MasterContext.class);
	
	private AbstractStaticMembership master_membership;
//	private final List<ISite> slave_stubs;
	
	/**
	 * Constructor using user-specified properties file.
	 * @param file path of the properties file; it cannot be {@code null}.
	 */
	public MasterContext(@Nonnull String file) {
		LOGGER.info("Using the properties file [{}] for [{}].", file, this.getClass().getSimpleName());
		this.master_membership = new MasterMembership(file);
	}
	
	@Override
	public @Nonnull Member self() {
		return this.master_membership.self();
	}
}
