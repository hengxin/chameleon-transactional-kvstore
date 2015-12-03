package network.membership;

import java.util.List;
import java.util.stream.Collectors;

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
	
	private final static String SELF = "self";
	
	private final Member self;
	private final List<Member> slaves;

	public MasterMembership(String file)
	{
		super(file);
		
		this.self = super.parseMember(MasterMembership.SELF);
		this.slaves = this.parseSlaves();
	}

	private List<Member> parseSlaves()
	{
		return super.prop.keySet().stream()
			.filter(key -> ! (MasterMembership.SELF.equals((String) key)))
			.map(slave_key -> super.parseMember(prop.getProperty((String) slave_key)))
			.collect(Collectors.toList());
	}
}
