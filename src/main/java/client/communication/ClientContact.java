package client.communication;

import java.rmi.Remote;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import master.IMaster;
import network.membership.ClientMembership;
import network.membership.Member;
import slave.ISlave;

/**
 * Maintain a contact list for the client of the server nodes 
 * (including the master and the slaves) via RMI.
 * 
 * @author hengxin
 * @date Created on 10-28-2015
 */
public class ClientContact
{
	private final static Logger LOGGER = LoggerFactory.getLogger(ClientContact.class);
	
	private final static String FILE = "membership-client.properties";
	private final ClientMembership client_membership = new ClientMembership(FILE);
	
	protected Map<IMaster, List<ISlave>> master_slaves_stub_map = new HashMap<>();
	
	public ClientContact()
	{
		this.client_membership.loadMembership();
		Map<Member, List<Member>> master_slaves_member_map = this.client_membership.getMasterSlavesMap();
		this.master_slaves_stub_map = this.transformAsRemoteStubs(master_slaves_member_map);
	}

	/**
	 * Contact the remote stubs (for RMI) of the masters and their individual slaves.
	 * 
	 * @param master_slaves_member_map A map from masters to their individual slaves 
	 * @return A map from {@link IMaster} to their individual {@link ISlave}
	 */
	private Map<IMaster, List<ISlave>> transformAsRemoteStubs(Map<Member, List<Member>> master_slaves_member_map)
	{
		return master_slaves_member_map.entrySet().stream()
			.<Entry<IMaster, List<ISlave>>>map(master_slaves_entry ->
			{
				Member master = master_slaves_entry.getKey();
				List<Member> slaves = master_slaves_entry.getValue();

				Remote master_stub = Member.parseStub(master);
				
				if(master_stub != null)
				{
					List<ISlave> slaves_stub = Member.parseStubs(slaves);
					LOGGER.debug("Client has contacted the master {} and its slaves {}.", master_stub, slaves_stub);
					return new AbstractMap.SimpleImmutableEntry<>((IMaster) master_stub, slaves_stub);
				}
				else 
				{
					LOGGER.warn("Fails to locate the master: {}. I will ignore it and all its slaves: {}.", master, slaves);
					return null;
				}
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		
	}
}