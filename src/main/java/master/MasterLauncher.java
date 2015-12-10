package master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.MemberParseException;
import exception.SiteException;
import master.context.MasterContext;
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
	
	private final static String MASTER_MEMBERSHIP_PROPERTIES_FILE = "master/membership-master.properties";

	/**
	 * Launch with default properties file, which is
	 * {@value #MASTER_MEMBERSHIP_PROPERTIES_FILE}.
	 * @throws SiteException 
	 * @throws MemberParseException 
	 */
	public MasterLauncher() throws SiteException, MemberParseException
	{
		this(MASTER_MEMBERSHIP_PROPERTIES_FILE);
	}
	
	/**
	 * Launch with user-specified properties file.
	 * @param file
	 * 		Path of the properties file.
	 * @throws SiteException 
	 * @throws MemberParseException 
	 */
	public MasterLauncher(String file) throws SiteException, MemberParseException
	{
		MasterContext context = new MasterContext(file);
		IRMI master = new SIMaster(context);
		master.export();
		
		LOGGER.info("Master has been successfully launched. The master stub is: {}", master);
	}
}
