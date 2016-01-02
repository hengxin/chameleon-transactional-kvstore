package client.context;

import kvs.compound.CompoundKey;
import master.IMaster;
import site.ISite;
import slave.ISlave;
import twopc.partitioning.IPartitioner;

/**
 * Provides context for transaction processing at the client side
 * in the <i>multiple-masters</i> setting.
 * The client maintains a map from {@link IMaster}s to their individual {@link ISlave}s.
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextMultiMaster extends AbstractClientContext {

	private final IPartitioner partitioner;
	
	public ClientContextMultiMaster(String file, IPartitioner partitioner) {
		super(file);
		this.partitioner = partitioner;
	}

	/**
	 * Return the {@link IMaster} which is responsible for the queried {@link CompoundKey}.
	 * @param ck	the {@link CompoundKey} queried
	 * @return		the {@link IMaster} responsible for the queried {@link CompoundKey}.
	 */
	public ISite getMaster(CompoundKey ck) {
		// TODO
		return null;
	}

	@Override
	public ISite getReadSite() {
		// TODO Auto-generated method stub
		return null;
	}

}
