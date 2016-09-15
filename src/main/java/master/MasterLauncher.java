package master;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import conf.SiteConfig;
import exception.SiteException;
import master.context.MasterContext;
import messaging.socket.SocketMsgBroadcastProducer;
import site.AbstractSite;

/**
 * Launch the master site by registering an object of {@link SIMaster} in the RMI registry.
 * 
 * @author hengxin
 * @date Created on 10-28-2015
 */
public class MasterLauncher {

	private final static Logger LOGGER = LoggerFactory.getLogger(MasterLauncher.class);

	/**
     * Constructor with default site.properties ({@value SiteConfig#DEFAULT_MASTER_SITE_PROPERTIES})
     * and default sa.properties ({@value SiteConfig#DEFAULT_SOCKET_ADDRESS_PROPERTIES}).
	 * @throws SiteException
	 */
	public MasterLauncher() {
		this(SiteConfig.DEFAULT_MASTER_SITE_PROPERTIES, SiteConfig.DEFAULT_SOCKET_ADDRESS_PROPERTIES);
	}
	
	/**
	 * Launch with user-specified properties file.
	 * @param siteProperties path of the site.properties file for site memebership
     * @param saProperties  path of the sa.properties file for socket addresses
	 * @throws SiteException 
	 */
	public MasterLauncher(@NotNull String siteProperties, @NotNull String saProperties) {
		MasterContext context = new MasterContext(siteProperties);
//		AbstractSite siMaster = new SIMaster(context);  // this constructor is with JMS communication

        AbstractSite siMaster = new SIMaster(context,
                new SocketMsgBroadcastProducer(saProperties));
		LOGGER.info("SIMaster [{}] has been successfully launched.", siMaster);
	}

}
