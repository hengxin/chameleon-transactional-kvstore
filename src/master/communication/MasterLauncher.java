package master.communication;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import master.IMaster;
import master.SIMaster;

/**
 * @author hengxin
 * @date 10-28-2015
 * 
 * Launch the master site by registering an object of {@link SIMaster} in the RMI registry.
 */
public class MasterLauncher
{
	public static final String SIMASTER_REGISTRY_NAME = "SIMaster";
	
	public static void main(String[] args)
	{
		try
		{
			IMaster master_stub = (IMaster) UnicastRemoteObject.exportObject(SIMaster.INSTANCE, 0);	// port 0: chosen at runtime
			LocateRegistry.getRegistry().rebind(SIMASTER_REGISTRY_NAME, master_stub);
		} catch(RemoteException re)
		{
			re.printStackTrace();
		}
	}
}
