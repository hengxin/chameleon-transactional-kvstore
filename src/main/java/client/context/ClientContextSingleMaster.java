package client.context;

import client.clientlibrary.partitioning.SingleMasterSettingPartitioner;
import site.ISite;

/**
 * Provides context for transaction processing at the client side
 * in the <i>single-master-multiple-slave</i> setting. 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextSingleMaster extends AbstractClientContext {

	public ClientContextSingleMaster() {
		this(DEFAULT_SITE_PROPERTIES_FILE, DEFAULT_COORD_FACTORY_PROPERTIES_FILE);
	}

	public ClientContextSingleMaster(String siteProperties, String cfProperties) {
		super(siteProperties, cfProperties);
		partitioner = new SingleMasterSettingPartitioner();
	}
	
	/**
	 * @return	the single master in the "single-master-multiple-slave" setting
	 */
	public ISite getMaster() { return membership.getMasterSites().get(0); }

}
