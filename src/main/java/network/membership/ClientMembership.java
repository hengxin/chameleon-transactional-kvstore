package network.membership;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import exception.MemberParseException;

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
	private Map<Member, List<Member>> master_slaves_map;
	
	public ClientMembership(String file) throws MemberParseException
	{
		super(file);
	}

	@Override
	public void loadMembershipFromProp()
	{
		this.master_slaves_map = this.loadMasterSlavesMap();
	}

	/**
	 * The .properties file consists of:
	 * <blockquote>  
	 * master = slave, slave, ...
	 * <p>
	 * master = slave, slave, ...
	 * <p>
	 * ...
	 * </blockquote>
	 * 
	 * @implNote
	 * 		FIXME The code is ugly. 
	 */
	private Map<Member, List<Member>> loadMasterSlavesMap()
	{
		return super.prop.entrySet().stream()
			.<Entry<Optional<Member>, List<Member>>>map(master_slaves_entry -> 
			{
				String master = (String) master_slaves_entry.getKey();
				String slaves = (String) master_slaves_entry.getValue();
				return new AbstractMap.SimpleImmutableEntry<>(Member.parseMember(master), Member.parseMembers(slaves));
			})
			.filter(entry -> entry.getKey().isPresent())
			.map(entry -> new AbstractMap.SimpleImmutableEntry<>(entry.getKey().get(), entry.getValue()))
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	public Member self()
	{
		// TODO identity of a client
		return null;
	}
	
	public Map<Member, List<Member>> getMasterSlavesMap()
	{
		return this.master_slaves_map;
	}
}
