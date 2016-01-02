package client.context;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kvs.compound.CompoundKey;
import site.ISite;

/**
 * Provides context for transaction processing at the client side
 * in the <i>single-master-multiple-slave</i> setting. 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextSingleMaster extends AbstractClientContext {

	private final static Logger LOGGER = LoggerFactory.getLogger(ClientContextSingleMaster.class);
	
	private final static String DEFAULT_CLIENT_PROPERTIES_FILE = "client/membership-client.properties";
	
	/**
	 * Constructor using the default properties file: {@value #DEFAULT_CLIENT_PROPERTIES_FILE}.
	 */
	public ClientContextSingleMaster() {
		this(DEFAULT_CLIENT_PROPERTIES_FILE);
	}
	
	/**
	 * Constructor using user-specified properties file.
	 * 
	 * @param file	path of the properties file.
	 */
	public ClientContextSingleMaster(String file) {
		super(file);
	}
	
	public ISite getMaster() {
		return super.clusters.get(0).getMaster();
	}
	
	/**
	 * @return	the only master in the "single-master-multiple-slave" setting
	 */
	@Override
	public ISite getMasterResponsibleFor(CompoundKey ck) {
		return this.getMaster();
	}

	/**
	 * In principle, the client is free to contact <i>any</i> site to read.
	 * In this particular implementation, it prefers an already cached slave. 
	 */
	@Override
	public ISite getReadSite(CompoundKey ck) {
		return super.cached_read_site.orElseGet(() -> {
				ISite read_site = super.clusters.get(0).getSiteForRead(); 
				super.cached_read_site = Optional.of(read_site);
				return read_site;
			});
	}

}
