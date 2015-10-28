package client.communication;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;

import master.IMaster;
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
			this.remote_master = (IMaster) LocateRegistry.getRegistry("");
		} catch (RemoteException re)
		{
			re.printStackTrace();
		}
	}

	public IMaster getRemote_master()
	{
		return this.remote_master;
	}
	
	
}
