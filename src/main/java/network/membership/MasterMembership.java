package network.membership;

import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private Member self;
	private List<Member> slaves;

	public MasterMembership(String file)
	{
		super(file);
	}

	/**
	 * Only one line in the .properties file: master = slave, slave, ... 
	 */
	@Override
	public void loadMembershipFromProp()
	{
		Entry<Object, Object> master_slaves_entry = super.prop.entrySet().iterator().next();

		this.self = Member.parseMember((String) master_slaves_entry.getKey());
		this.slaves = Member.parseMembers((String) master_slaves_entry.getValue());
	}
}
