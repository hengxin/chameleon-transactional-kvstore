package client.context;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.ClusterActive;
import exception.ContextException;
import exception.network.membership.MemberParseException;
import site.ISite;

/**
 * Provides context for transaction processing at the client side
 * in the <i>single-master-multiple-slave</i> setting. 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextSingleMaster extends AbstractClientContext
{
	private final static Logger LOGGER = LoggerFactory.getLogger(ClientContextSingleMaster.class);
	
	private final static String DEFAULT_CLIENT_PROPERTIES_FILE = "client/membership-client.properties";

	private Optional<ISite> cached_read_site = Optional.empty();	
	
	/**
	 * Constructor using the default properties file: {@value #DEFAULT_CLIENT_PROPERTIES_FILE}.
	 * 
	 * @throws ContextException 
	 * 		Failed to create this context because no master is available.
	 * @throws MemberParseException 
	 */
	public ClientContextSingleMaster() throws ContextException, MemberParseException
	{
		this(DEFAULT_CLIENT_PROPERTIES_FILE);
	}
	
	/**
	 * Constructor using user-specified properties file.
	 * 
	 * @param file
	 * 		Path of the properties file.
	 * @throws ContextException 
	 * 		Failed to create this context because no master is available.
	 * @throws MemberParseException 
	 */
	public ClientContextSingleMaster(String file) throws ContextException, MemberParseException
	{
		super(file);
	}
	
	public ISite getMaster()
	{
		return super.clusters.get(0).getMaster();
	}

	/**
	 * In principle, the client is free to contact <i>any</i> site to read.
	 * In this particular implementation, it prefers an already cached slave. 
	 */
	@Override
	public ISite getReadSite()
	{
		return this.cached_read_site.orElseGet(() ->
			{
				ClusterActive cluster = super.clusters.get(0);
				ISite read_site = cluster.hasNoSlaves() ? cluster.getMaster() : cluster.getRandomSlave(); 
				this.cached_read_site = Optional.of(read_site);
				return read_site;
			});
	}
}
