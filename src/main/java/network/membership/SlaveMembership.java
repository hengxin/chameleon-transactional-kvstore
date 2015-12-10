package network.membership;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.MemberParseException;

/**
 * A slave needs to know itself and <i>its</i> master (not all the masters). 
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class SlaveMembership extends AbstractStaticMembership
{
	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveMembership.class);
	
	private Member master;
	
	public SlaveMembership(String file) throws MemberParseException
	{
		super(file);
	}

	@Override
	public void loadMembershipFromProp() throws MemberParseException
	{
		Entry<Object, Object> self_master_entry = super.prop.entrySet().iterator().next();

		String self = (String) self_master_entry.getKey();
		String master = (String) self_master_entry.getValue();
		
		super.self = Member.parseMember(self).orElseThrow(() -> new MemberParseException(String.format("Failed to parse (%s) as self.", self)));
		this.master = Member.parseMember(master).orElseThrow(() -> new MemberParseException(String.format("Failed to parse (%s) as master.", master)));

		LOGGER.info("I am a slave: {}. My master is: {}.", self, master);
	}

	public Member getMaster()
	{
		return master;
	}

}
