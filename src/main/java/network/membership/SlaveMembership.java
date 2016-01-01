package network.membership;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.network.membership.MasterMemberParseException;
import exception.network.membership.MemberParseException;
import exception.network.membership.SlaveMemberParseException;

/**
 * A slave needs to know itself and <i>its</i> master (not all the masters). 
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class SlaveMembership extends AbstractStaticMembership {

	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveMembership.class);
	
	private Member master;
	
	public SlaveMembership(String file) throws MemberParseException {
		super(file);
	}

	/**
	 * Only one line to parse: slave = master
	 * @throws SlaveMemberParseException	if it fails to parse the slave itself
	 * @throws MasterMemberParseException	if it fails to parse its master
	 * @throws MemberParseException			if super#prop is in ill-format.
	 */
	@Override
	public void parseMembershipFromProp() 
			throws SlaveMemberParseException, MasterMemberParseException, MemberParseException {
		if (super.prop.size() != 1)
			throw new MemberParseException(String.format("Failed to parse membership from [%s]: It should a single line of the (slave = master) format.", super.file));

		String slave = super.prop.stringPropertyNames().toArray(new String[1])[0];
		String master = super.prop.getProperty(slave);
		
		// parse the slave itself
		try{
			super.self = Member.parseMember(slave);
		} catch (MemberParseException mpe) {
			throw new SlaveMemberParseException(mpe);
		}
		
		// parse its master
		try{
			this.master = Member.parseMember(master);
		} catch (MemberParseException mpe) {
			throw new MasterMemberParseException(mpe);
		}

		LOGGER.info("I am a slave: [{}]. My master is: [{}].", slave, master);
	}

	public Member getMaster() {
		return master;
	}

}
