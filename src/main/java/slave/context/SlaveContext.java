package slave.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.IContext;
import exception.MemberParseException;
import network.membership.AbstractStaticMembership;
import network.membership.Member;
import network.membership.SlaveMembership;

/**
 * Context for slave sites.
 * @author hengxin
 * @date Created on Dec 13, 2015
 */
public class SlaveContext implements IContext
{
	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveContext.class);
	
	private final AbstractStaticMembership slave_membership;
//	private final Optional<ISite> master;	// the slave is not forced to contact its master.

	/**
	 * Constructor using user-specified configuration file.
	 * @param file
	 * @throws MemberParseException 
	 */
	public SlaveContext(String file) throws MemberParseException
	{
		LOGGER.info("Using the properties file ({}) for SlaveContext.", file);
		this.slave_membership = new SlaveMembership(file);
//		this.master = Member.parseStub(((SlaveMembership) this.slave_membership).getMaster());
	}

	@Override
	public Member self()
	{
		return this.slave_membership.self();
	}

}
