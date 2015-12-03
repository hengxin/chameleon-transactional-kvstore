package client.communication;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import master.IMaster;
import master.communication.MasterLauncher;
import network.membership.StaticMembership;
import slave.ISlave;

/**
 * Maintain a contact list for the client of the server nodes 
 * (including the master and the slaves) via RMI.
 * 
 * @author hengxin
 * @date Created on 10-28-2015
 */
public enum ClientContacts
{
	INSTANCE;
	
	private final Logger LOGGER = LoggerFactory.getLogger(ClientContacts.class);
	
	private IMaster master;
	private List<ISlave> remote_slaves;
	
	private ClientContacts()
	{
		try
		{
			this.master = (IMaster) LocateRegistry.getRegistry(StaticMembership.INSTANCE.getMasterAddr()).lookup(MasterLauncher.SIMASTER_REGISTRY_NAME);
			LOGGER.debug("Client has contacted the master: {}.", this.master.toString());
		} catch (RemoteException | NotBoundException e)
		{
			LOGGER.debug("It fails for the client to contact the master: {}.", e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
	}

	public IMaster getMaster()
	{
		return this.master;
	}
	
}
