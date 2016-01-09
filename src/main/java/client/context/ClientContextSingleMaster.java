package client.context;

import client.clientlibrary.partitioning.SingleMasterSettingPartitioner;
import rmi.IRemoteSite;

/**
 * Provides context for transaction processing at the client side
 * in the <i>single-master-multiple-slave</i> setting. 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextSingleMaster extends AbstractClientContext {

	/**
	 * Constructor with the default .properties file: {@value #DEFAULT_CLIENT_PROPERTIES_FILE}.
	 */
	public ClientContextSingleMaster() {
		this(DEFAULT_CLIENT_PROPERTIES_FILE);
	}
	
	/**
	 * Constructor using user-specified .properties file.
	 * 
	 * @param file	path of the .properties file.
	 */
	public ClientContextSingleMaster(String file) {
		super(file);
		this.partitioner = new SingleMasterSettingPartitioner();
	}
	
	/**
	 * @return	the single master in the "single-master-multiple-slave" setting
	 */
	public IRemoteSite getMaster() {
		return super.clusters.get(0).getMaster();
	}

}
