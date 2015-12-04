package network.membership;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A client needs to know all the masters and their individual slaves.
 * In this implementation, all of these membership information is loaded from this .properties file.
 * Each line is in the format of "master = a list of its slaves separated by commas"

 * An alternative approach (which I don't take now) is:
 * A client initially maintains a (full) list of all masters.
 * And it asks these masters for their individual slaves at runtime.
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class ClientMembership extends AbstractStaticMembership
{
	private final static Logger LOGGER = LoggerFactory.getLogger(ClientMembership.class);

	private Map<Member, List<Member>> master_slaves;
	
	public ClientMembership(String file)
	{
		super(file);
	}

	@Override
	public void loadMembershipFromProp()
	{
		this.master_slaves = this.fillMasterSlaves();
	}

	private Map<Member, List<Member>> fillMasterSlaves()
	{
		return super.prop.entrySet().stream()
			.<Entry<Member, List<Member>>>map(master_slaves_entry -> 
				new AbstractMap.SimpleImmutableEntry<>(Member.parseMember((String) master_slaves_entry.getKey()), 
						Member.parseMembers((String) master_slaves_entry.getValue())))
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
}
