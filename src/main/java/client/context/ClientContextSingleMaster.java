package client.context;

import org.jetbrains.annotations.NotNull;

import client.clientlibrary.partitioning.SingleMasterSettingPartitioner;
import site.ISite;

import static conf.SiteConfig.DEFAULT_CLIENT_COORD_FACTORY_PROPERTIES_FILE;
import static conf.SiteConfig.DEFAULT_CLIENT_SITE_PROPERTIES_FILE;
import static conf.SiteConfig.DEFAULT_TO_PROPERTIES;

/**
 * Provides context for transaction processing at the client side
 * in the <i>single-master-multiple-slave</i> setting. 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextSingleMaster extends AbstractClientContext {
    private static final long serialVersionUID = -3909286327773778797L;

    public ClientContextSingleMaster() {
		this(DEFAULT_CLIENT_SITE_PROPERTIES_FILE,
                DEFAULT_CLIENT_COORD_FACTORY_PROPERTIES_FILE,
                DEFAULT_TO_PROPERTIES);
	}

	public ClientContextSingleMaster(@NotNull String siteProperties,
                                     @NotNull String cfProperties,
                                     @NotNull String toProperties) {
		super(siteProperties, cfProperties, toProperties);
		partitioner = new SingleMasterSettingPartitioner();
	}
	
	/**
	 * @return	the single master in the "single-master-multiple-slave" setting
	 */
	public ISite getMaster() { return membership.getMasterSites().get(0); }

}
