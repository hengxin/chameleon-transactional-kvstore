package master.communication;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import master.IMaster;
import master.SIMaster;
import network.membership.StaticMembership;

/**
 * @author hengxin
 * @date 10-28-2015
 * 
 * Launch the master site by registering an object of {@link SIMaster} in the RMI registry.
 */
public class MasterLauncher
{
	private static final Logger logger = LoggerFactory.getLogger(MasterLauncher.class);

	public static final String SIMASTER_REGISTRY_NAME = "SIMaster";
	private static final int REGISTRY_PORT = 1099;
	
	public static void main(String[] args)
	{
		System.setProperty("java.rmi.server.hostname", StaticMembership.INSTANCE.getMasterAddr());
		
		IMaster master_stub = null;
		try
		{
			master_stub = (IMaster) UnicastRemoteObject.exportObject(SIMaster.INSTANCE, 0);	// port 0: chosen at runtime
			LocateRegistry.createRegistry(MasterLauncher.REGISTRY_PORT).rebind(SIMASTER_REGISTRY_NAME, master_stub);
		} catch(RemoteException re)
		{
			re.printStackTrace();
		}
		
		logger.info("Master has been successfully launched. The master stub is: {}", master_stub.toString());
	}
}
