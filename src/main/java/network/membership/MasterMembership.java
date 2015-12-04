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

		String master = (String) master_slaves_entry.getKey();
		String slaves = (String) master_slaves_entry.getValue();
		
		LOGGER.info("The master is {}. Its slaves are {}.", master, slaves);

		this.self = Member.parseMember(master);
		this.slaves = Member.parseMembers(slaves);
	}

	public Member getSelf()
	{
		return self;
	}

	public List<Member> getSlaves()
	{
		return slaves;
	}
}
