package master.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import master.IMaster;
import master.SIMaster;
import rmi.IRMI;

/**
 * Launch the master site by registering an object of {@link SIMaster} in the RMI registry.
 * 
 * @author hengxin
 * @date Created on 10-28-2015
 */
public class MasterLauncher
{
	private final static Logger LOGGER = LoggerFactory.getLogger(MasterLauncher.class);
	
	private final static String MASTER_MEMBERSHIP_PROPERTIES_FILE = "./master/membership-master.properties";

	private IMaster master;
	private final MasterContext context;
	
	/**
	 * Launch with default properties file, which is
	 * {@value #MASTER_MEMBERSHIP_PROPERTIES_FILE} = "./master/membership-master.properties".
	 */
	public MasterLauncher()
	{
		this.context = new MasterContext(MASTER_MEMBERSHIP_PROPERTIES_FILE);
		this.launch();
	}
	
	/**
	 * Launch with user-specified properties file.
	 * @param file
	 * 		Path of the properties file.
	 */
	public MasterLauncher(String file)
	{
		this.context = new MasterContext(file);
		this.launch();
	}
	
	private void launch()
	{
		this.master = new SIMaster(this.context);
		((IRMI) master).export();
		
		LOGGER.info("Master has been successfully launched. The master stub is: {}", master.toString());
	}

	public void reclaim()
	{
		((IRMI) master).reclaim();
	}
	
	public static void main(String[] args)
	{
	}
}
