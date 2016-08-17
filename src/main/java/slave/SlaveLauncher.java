package slave;

import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.SiteException;
import site.AbstractSite;
import slave.context.SlaveContext;

public class SlaveLauncher {

	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveLauncher.class);
	@Language("Properties")
    private final static String MASTER_MEMBERSHIP_PROPERTIES_FILE = "slave/site.properties";

	/**
	 * Launch with default properties file, which is
	 * {@value #MASTER_MEMBERSHIP_PROPERTIES_FILE}.
	 * @throws SiteException 
	 */
	public SlaveLauncher() throws SiteException {
		this(MASTER_MEMBERSHIP_PROPERTIES_FILE);
	}
	
	/**
	 * Launch with user-specified properties file.
	 * @param file path of the properties file.
	 * @throws SiteException 
	 */
	public SlaveLauncher(String file) throws SiteException {
		SlaveContext context = new SlaveContext(file);
		AbstractSite slave = new RCSlave(context);

		LOGGER.info("Slave [{}] has been successfully launched.", slave);
	}

}
