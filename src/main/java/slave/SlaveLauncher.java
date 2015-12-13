package slave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.MemberParseException;
import exception.SiteException;
import jms.AbstractJMSParticipant;
import jms.master.JMSCommitLogPublisher;
import jms.slave.JMSCommitLogSubscriber;
import master.MasterLauncher;
import master.SIMaster;
import master.context.MasterContext;
import site.AbstractSite;

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
		MasterContext context = new MasterContext(file);
		AbstractSite slave = new RCSlave(context);
		
		AbstractJMSParticipant subscriber = new JMSCommitLogSubscriber();
		slave.registerAsJMSParticipant(subscriber);
		
		slave.export();
		
		LOGGER.info("Slave [{}] has been successfully launched.", slave);
	}
}
