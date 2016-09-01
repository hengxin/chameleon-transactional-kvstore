package slave;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import conf.SiteConfig;
import exception.SiteException;
import messaging.socket.SocketMsgListener2;
import site.AbstractSite;
import slave.context.SlaveContext;

public class SlaveLauncher {
	private final static Logger LOGGER = LoggerFactory.getLogger(SlaveLauncher.class);

	/**
     * Constructor with default site.properties ({@value SiteConfig#DEFAULT_SLAVE_SITE_PROPERTIES})
     * and default sp.properties ({@value SiteConfig#DEFAULT_SOCKET_PORT_PROPERTIES}).
	 * @throws SiteException
	 */
	public SlaveLauncher() throws SiteException {
		this(SiteConfig.DEFAULT_SLAVE_SITE_PROPERTIES, SiteConfig.DEFAULT_SOCKET_PORT_PROPERTIES);
	}
	
	/**
	 * Launch with user-specified properties file.
	 * @param siteProperties path of the properties file.
	 * @throws SiteException 
	 */
	public SlaveLauncher(String siteProperties, String spProperties) throws SiteException {
		SlaveContext context = new SlaveContext(siteProperties);
//		AbstractSite slave = new RCSlave(context);  // the default constructor uses JMS by default
        AbstractSite slave = new RCSlave(context, new SocketMsgListener2(spProperties));

		LOGGER.info("Slave [{}] has been successfully launched.", slave);
	}

}
