package client.context;

import java.util.List;
import java.util.Map.Entry;

import master.IMaster;
import slave.ISlave;

/**
 * Provides context for transaction processing at the client side
 * and in the <i>single-master-multiple-slaves</i> setting, including:
 * <p>
 * <ul>
 * <li> {@link #master}: the single {@link IMaster}
 * <li> {@link #remote_slaves}: a list of {@link ISlave}s
 * </ul>
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextSingleMaster extends ClientContext
{
	private final static String DEFAULT_CLIENT_PROPERTIES_FILE = "client/membership-client.properties";

	private final IMaster master;
	private final List<ISlave> remote_slaves;
	
	/**
	 * Constructor using the default properties file:
	 * {@value #DEFAULT_CLIENT_PROPERTIES_FILE}.
	 */
	public ClientContextSingleMaster()
	{
		super(DEFAULT_CLIENT_PROPERTIES_FILE);
		
		Entry<IMaster, List<ISlave>> master_slaves_entry = super.master_slaves_stub_map.entrySet().iterator().next();
		this.master = master_slaves_entry.getKey();
		this.remote_slaves = master_slaves_entry.getValue();
	}
	
	/**
	 * Constructor using user-specified properties file.
	 * @param file
	 * 		Path of the properties file.
	 */
	public ClientContextSingleMaster(String file)
	{
		super(file);

		Entry<IMaster, List<ISlave>> master_slaves_entry = super.master_slaves_stub_map.entrySet().iterator().next();
		this.master = master_slaves_entry.getKey();
		this.remote_slaves = master_slaves_entry.getValue();
	}
	
	public IMaster getMaster()
	{
		return this.master;
	}
}
