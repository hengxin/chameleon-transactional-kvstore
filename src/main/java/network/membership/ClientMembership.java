package network.membership;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A client needs to know all the sites, including all the masters and their individual slaves.
 * Thus, it maintains a map from masters to a list of their slaves.
 * 
 * In this implementation, a client initially maintains a (full) list of all masters.
 * It will ask these masters for their individual slaves at runtime.
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class ClientMembership extends AbstractStaticMembership
{
	private final static Logger LOGGER = LoggerFactory.getLogger(ClientMembership.class);

	private final Map<Member, List<Member>> master_slaves;
	
	public ClientMembership(String file)
	{
		super(file);
		this.master_slaves = null;
	}

	private List<Member> findSlavesOf(Member master)
	{
		return null;
	}
	
	private Map<Member, List<Member>> fillMasterSlaves()
	{
		return null;
	}
}
