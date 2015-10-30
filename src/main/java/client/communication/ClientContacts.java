package client.communication;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

import master.IMaster;
import master.communication.MasterLauncher;
import slave.ISlave;

/**
 * @author hengxin
 * @date 10-28-2015
 * 
 * Establish a contact list for the client to the server nodes (including the master and the slaves) via RMI.
 */
public enum ClientContacts
{
	INSTANCE;
	
	private IMaster remote_master;
	private List<ISlave> remote_slaves;
	
	private ClientContacts()
	{
		try
		{
            // TODO: host name for the remote master
			this.remote_master = (IMaster) LocateRegistry.getRegistry(MasterLauncher.MASTER_IP).lookup(MasterLauncher.SIMASTER_REGISTRY_NAME);
		} catch (RemoteException | NotBoundException re)
		{
			re.printStackTrace();
		}
	}

	public IMaster getRemote_master()
	{
		return this.remote_master;
	}
	
	
}
