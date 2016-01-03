package client.context;

import twopc.partitioning.IPartitioner;

/**
 * Provides context for transaction processing at the client side
 * in the <i>multiple-masters</i> setting.
 * 
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class ClientContextMultiMaster extends AbstractClientContext {
	
	/**
	 * Constructor with the default .properties file: {@value #DEFAULT_CLIENT_PROPERTIES_FILE}
	 * and user-specified {@link IPartitioner}.
	 * @param partitioner	{@link IPartitioner} for keyspace partition strategy
	 */
	public ClientContextMultiMaster(IPartitioner partitioner) {
		this(DEFAULT_CLIENT_PROPERTIES_FILE, partitioner);
	}
	
	/**
	 * Constructor with user-specified .properties file and keyspace {@link IPartitioner}.
	 * @param file	.properties file name
	 * @param partitioner	{@link IPartitioner} for keyspace partition strategy
	 */
	public ClientContextMultiMaster(String file, IPartitioner partitioner) {
		super(file);
		super.partitioner = partitioner;
	}

}
