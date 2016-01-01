package network.membership;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.network.membership.MasterMemberParseException;
import exception.network.membership.MemberParseException;
import exception.network.membership.SlaveMemberParseException;

/**
 * A master needs to know itself and all of <i>its</i> slaves.
 * However, in this implementation, a master does not necessarily know other masters
 * and their slaves.
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class MasterMembership extends AbstractStaticMembership
{
	private final static Logger LOGGER = LoggerFactory.getLogger(MasterMembership.class);
	
	private List<Member> slaves;

	public MasterMembership(String file) throws MemberParseException
	{
		super(file);
	}

	/**
	 * Only one line to parse: master = slave, slave, ... 
	 * @throws MasterMemberParseException	if it fails to parse the master itself
	 * @throws SlaveMemberParseException	if it fails to parse some of slaves
	 * @throws MemberParseException			if super#prop is in ill-format.
	 */
	@Override
	public void parseMembershipFromProp() 
			throws MasterMemberParseException, SlaveMemberParseException, MemberParseException
	{
		if (super.prop.size() != 1)
			throw new MemberParseException(String.format("Failed to load membership from [%s]: It should contain a single line of the (master = slave, slave, ...) format.", super.file));
		
		String master = super.prop.stringPropertyNames().toArray(new String[1])[0];
		String slaves = super.prop.getProperty(master);

		// parse the master itself
		try{
			super.self = Member.parseMember(master);
		} catch(MemberParseException mpe) {
			throw new MasterMemberParseException(mpe);
		}
		
		// parse slaves
		try{
			this.slaves = Member.parseMembers(slaves);
		} catch(MemberParseException mpe) {
			throw new SlaveMemberParseException(mpe);
		}

		LOGGER.info("I am a master: {}. My slaves are: {}.", master, slaves);
	}

	public List<Member> getSlaves()
	{
		return slaves;
	}
}
