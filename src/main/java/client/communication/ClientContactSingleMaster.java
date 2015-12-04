package client.communication;

import java.util.List;
import java.util.Map.Entry;

import master.IMaster;
import slave.ISlave;

/**
 * Contact list for client in the <i>single-master</i> setting.
 * The client maintains an {@link IMaster} and a list of {@link ISlave}s.
 * <p>
 * <b>Note:</b> Using Singleton design pattern. This class is not intended for multiple threads. 
 * It is <i>not</i> thread-safe.
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContactSingleMaster extends ClientContact
{
	private static ClientContactSingleMaster instance = null;
	
	private final IMaster master;
	private final List<ISlave> remote_slaves;
	
	private ClientContactSingleMaster()
	{
		super();
		
		Entry<IMaster, List<ISlave>> master_slaves_entry = super.master_slaves_stub_map.entrySet().iterator().next();
		this.master = master_slaves_entry.getKey();
		this.remote_slaves = master_slaves_entry.getValue();
	}
	
	public static ClientContactSingleMaster getInstance()
	{
		if(instance == null)
			return new ClientContactSingleMaster();
		return instance;
	}
	
	public IMaster getMaster()
	{
		return this.master;
	}
}
