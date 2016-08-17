package master;

import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.SiteException;
import master.context.MasterContext;
import site.AbstractSite;

/**
 * Launch the master site by registering an object of {@link SIMaster} in the RMI registry.
 * 
 * @author hengxin
 * @date Created on 10-28-2015
 */
public class MasterLauncher {

	private final static Logger LOGGER = LoggerFactory.getLogger(MasterLauncher.class);
	@Language("Properties")
    private final static String MASTER_MEMBERSHIP_PROPERTIES_FILE = "master/site.properties";

	/**
	 * Launch with default properties file, which is
	 * {@value #MASTER_MEMBERSHIP_PROPERTIES_FILE}.
	 * @throws SiteException 
	 */
	public MasterLauncher() {
		this(MASTER_MEMBERSHIP_PROPERTIES_FILE);
	}
	
	/**
	 * Launch with user-specified properties file.
	 * @param file path of the properties file.
	 * @throws SiteException 
	 */
	public MasterLauncher(String file) {
		MasterContext context = new MasterContext(file);
		AbstractSite siMaster = new SIMaster(context);

		LOGGER.info("SIMaster [{}] has been successfully launched.", siMaster);
	}

}
