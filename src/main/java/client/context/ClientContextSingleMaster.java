package client.context;

import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exception.ContextException;
import exception.MemberParseException;
import site.ISite;

/**
 * Provides context for transaction processing at the client side
 * and in the <i>single-master-multiple-slaves</i> setting, including:
 * <p>
 * <ul>
 * <li> {@link #master}: the single master
 * <li> {@link #slaves}: a list of slaves
 * </ul>
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextSingleMaster extends AbstractClientContext
{
	private final static Logger LOGGER = LoggerFactory.getLogger(ClientContextSingleMaster.class);
	
	private final static String DEFAULT_CLIENT_PROPERTIES_FILE = "client/membership-client.properties";

	private ISite master;
	private List<ISite> slaves;
	
	private Optional<ISite> cached_read_site = Optional.empty();	
	
	/**
	 * Constructor using the default properties file:
	 * {@value #DEFAULT_CLIENT_PROPERTIES_FILE}.
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
		this.setMasterSlaves();
	}
	
	/**
	 * Set {@link #master} and {@link #slaves} for RMI accesses.
	 * 
	 * @throws ContextException 
	 * 		Failed to create this context because no master is available.
	 */
	private void setMasterSlaves() throws ContextException
	{
		try
		{
			Entry<ISite, List<ISite>> master_slaves_entry = super.master_slaves_stub_map.entrySet().iterator().next();
			this.master = master_slaves_entry.getKey();
			this.slaves = master_slaves_entry.getValue();
		} catch (NoSuchElementException nsee)
		{
			throw new ContextException("No master available.", nsee.getCause());
		}
	}
	
	public ISite getMaster()
	{
		return this.master;
	}

	/**
	 * In principle, the client is free to contact <i>any</i> site to read.
	 * In this particular implementation, it prefers a nearby slave. 
	 */
	@Override
	public ISite getReadSite()
	{
		return this.cached_read_site.orElseGet(() ->
			{
				ISite site = this.slaves.isEmpty() ? this.master : this.slaves.get(new Random().nextInt(this.slaves.size()));
				this.cached_read_site = Optional.of(site);
				return site;
			});
	}
}
