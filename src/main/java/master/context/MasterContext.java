package master.context;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.IContext;
import network.membership.AbstractStaticMembership;
import network.membership.MasterMembership;
import network.membership.Member;
import slave.ISlave;

/**
 * Provides context for master sites, including
 * <p>
 * <ul>
 * <li> {@link #slave_stubs}: a list of its slaves
 * </ul>
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class MasterContext implements IContext
{
	private final static Logger LOGGER = LoggerFactory.getLogger(MasterContext.class);
	
	private final AbstractStaticMembership master_membership;
	private final List<ISlave> slave_stubs;
	
	/**
	 * Constructor using user-specified properties file.
	 * @param file
	 * 		Path of the properties file.
	 */
	public MasterContext(String file)
	{
		LOGGER.info("Using the properties file ({}) for MasterContext.", file);
		this.master_membership = new MasterMembership(file);
		this.slave_stubs = Member.parseStubs(((MasterMembership) this.master_membership).getSlaves());
	}
	
	@Override
	public Member self()
	{
		return this.master_membership.getSelf();
	}
}
