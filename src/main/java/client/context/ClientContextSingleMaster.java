package client.context;

import java.rmi.Remote;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.net.server.Client;
import exception.ContextException;
import master.IMaster;
import slave.ISlave;

/**
 * Provides context for transaction processing at the client side
 * and in the <i>single-master-multiple-slaves</i> setting, including:
 * <p>
 * <ul>
 * <li> {@link #master}: the single {@link IMaster}
 * <li> {@link #slaves}: a list of {@link ISlave}s
 * </ul>
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextSingleMaster extends AbstractClientContext
{
	private final static Logger LOGGER = LoggerFactory.getLogger(ClientContextSingleMaster.class);
	
	private final static String DEFAULT_CLIENT_PROPERTIES_FILE = "client/membership-client.properties";

	private IMaster master;
	private List<ISlave> slaves;
	
	private Optional<Remote> cached_read_server;	
	
	/**
	 * Constructor using the default properties file:
	 * {@value #DEFAULT_CLIENT_PROPERTIES_FILE}.
	 * 
	 * @throws ContextException 
	 * 		Failed to create this context because no master is available.
	 */
	public ClientContextSingleMaster() throws ContextException
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
	 */
	public ClientContextSingleMaster(String file) throws ContextException
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
			Entry<IMaster, List<ISlave>> master_slaves_entry = super.master_slaves_stub_map.entrySet().iterator().next();
			this.master = master_slaves_entry.getKey();
			this.slaves = master_slaves_entry.getValue();
		} catch (NoSuchElementException nsee)
		{
			throw new ContextException("No master available.", nsee.getCause());
		}
	}
	
	public IMaster getMaster()
	{
		return this.master;
	}

	/**
	 * In principle, the client is free to contact <i>any</i> site to read.
	 * In this particular implementation, it prefers a nearby slave. 
	 */
	@Override
	public Remote getReadServer()
	{
		// TODO
		return this.cached_read_server.orElseGet(null);
	}
}
