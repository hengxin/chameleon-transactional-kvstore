package client.communication;

import kvs.compound.CompoundKey;
import master.IMaster;
import slave.ISlave;

/**
 * Provides context for transaction processing at the client side
 * in the <i>multiple-masters</i> setting.
 * The client maintains a map from {@link IMaster}s to their individual {@link ISlave}s.
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextMultiMaster extends ClientContext
{
	/**
	 * Return the {@link IMaster} which is responsible for the queried {@link CompoundKey}.
	 * @param ck
	 * 		the {@link CompoundKey} queried
	 * @return
	 * 		the {@link IMaster} responsible for the queried {@link CompoundKey}.
	 */
	public IMaster getMaster(CompoundKey ck)
	{
		return null;
	}

}
