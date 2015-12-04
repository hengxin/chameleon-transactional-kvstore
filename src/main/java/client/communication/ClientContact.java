package client.communication;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
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

				IMaster master_stub = null;
				try
				{
					master_stub = (IMaster) this.getStubFor(master);
				} catch (Exception e)
				{
					LOGGER.warn("Client fails to contact the remote master: {}. \\ Client will ignore the master and all of its slaves: {}. \\ Please check the details: {}", master, slaves, e.getMessage());
					e.printStackTrace();
				}
				
				List<ISlave> slaves_stub = null;
				if(master_stub != null)
				{
				  slaves_stub = slaves.stream()
					.<ISlave>map(slave -> 
						{ 
							try
							{
								return (ISlave) this.getStubFor(slave);
							} catch (Exception e)
							{
								LOGGER.warn("Client fails to contact the slave: {}. Client will ignore it. \\ Please check the details: {}", slave, e.getMessage());
								e.printStackTrace();
								return null;
							} 
						})
					.collect(Collectors.toList());
				}
				
				if(master_stub != null)
					LOGGER.debug("Client has contacted the master {} and its slaves {}.", master_stub, slaves_stub);

				return master_stub == null? null : new AbstractMap.SimpleImmutableEntry<>(master_stub, slaves_stub);
			})
			.filter(Objects::nonNull)
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		
	}
	public IMaster getMaster()
	{
		return null;
	}
	
	private Remote getStubFor(Member member) throws AccessException, RemoteException, NotBoundException
	{
		return LocateRegistry.getRegistry(member.getAddrIp()).lookup(member.getRmiRegistryName());
	}
}
