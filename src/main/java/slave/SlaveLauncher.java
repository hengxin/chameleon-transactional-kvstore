package slave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.SiteException;
import exception.network.membership.MemberParseException;
import site.AbstractSite;
import slave.context.SlaveContext;

public class SlaveLauncher
{
	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveLauncher.class);
	
	private final static String MASTER_MEMBERSHIP_PROPERTIES_FILE = "slave/membership-slave.properties";

	/**
	 * Launch with default properties file, which is
	 * {@value #MASTER_MEMBERSHIP_PROPERTIES_FILE}.
	 * @throws SiteException 
	 * @throws MemberParseException 
	 */
	public SlaveLauncher() throws SiteException, MemberParseException
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
	public SlaveLauncher(String file) throws SiteException, MemberParseException
	{
		SlaveContext context = new SlaveContext(file);
		AbstractSite slave = new RCSlave(context);
		
		slave.export();
		
		LOGGER.info("Slave [{}] has been successfully launched.", slave);
	}
}
