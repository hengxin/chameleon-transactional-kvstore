package master.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.IContext;
import exception.network.membership.MasterMemberParseException;
import exception.network.membership.MemberParseException;
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
	 * @param file
	 * 		Path of the properties file.
	 */
	public MasterContext(String file) {
		LOGGER.info("Using the properties file [{}] for [{}].", file, this.getClass().getSimpleName());

		try{
			this.master_membership = new MasterMembership(file);
		} catch (MasterMemberParseException mmpe) {
			LOGGER.error("Failed to create master context.", mmpe);
			System.exit(1);
		} catch (MemberParseException mpe) {
			LOGGER.warn("Some slave sites cannot be parsed.", mpe);
		}

		// FIXME exception handling
//		this.slave_stubs = AbstractSite.locateRMISites(((MasterMembership) this.master_membership).getSlaves());
//		LOGGER.info("Successfully parse stubs of my slaves [{}]", this.slave_stubs);
	}
	
	@Override
	public Member self() {
		return this.master_membership.self();
	}
}
