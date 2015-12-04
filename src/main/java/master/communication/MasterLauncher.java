package master.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import master.IMaster;
import master.SIMaster;
import rmi.IRMI;

/**
 * @author hengxin
 * @date 10-28-2015
 * 
 * Launch the master site by registering an object of {@link SIMaster} in the RMI registry.
 */
public class MasterLauncher
{
	private final static Logger logger = LoggerFactory.getLogger(MasterLauncher.class);

	public static void main(String[] args)
	{
		System.setProperty("java.rmi.server.hostname", MasterContact.INSTANCE.getMasterAddrIp());
		
		IMaster master = new SIMaster();
		((IRMI) master).export();
		
		logger.info("Master has been successfully launched. The master stub is: {}", master.toString());
	}
}
