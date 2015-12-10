package network.membership;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.MemberParseException;

/**
 * A master needs to know itself and all of <i>its</i> slaves.
 * In this implementation, a master does not necessarily know other masters
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
	 * Only one line in the .properties file: master = slave, slave, ... 
	 */
	@Override
	public void loadMembershipFromProp() throws MemberParseException
	{
		Iterator<Entry<Object, Object>> master_slaves_iter = super.prop.entrySet().iterator();
		if(! master_slaves_iter.hasNext())
			throw new MemberParseException(String.format("Failed to load membership from (%s). Is it blank? It should be of the (master = slave, slave, ...) format.", super.file));
		
		Entry<Object, Object> master_slaves_entry = master_slaves_iter.next();

		String master = (String) master_slaves_entry.getKey();
		String slaves = (String) master_slaves_entry.getValue();

		super.self = Member.parseMember(master).orElseThrow(() -> new MemberParseException(String.format("Failed to parse (%s) as self.", self)));
		this.slaves = Member.parseMembers(slaves);

		LOGGER.info("I am a master: {}. My slaves are: {}.", master, slaves);
	}

	public List<Member> getSlaves()
	{
		return slaves;
	}
}
