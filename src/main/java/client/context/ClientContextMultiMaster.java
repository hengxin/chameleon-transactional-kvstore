package client.context;

import java.util.Optional;

import kvs.compound.CompoundKey;
import site.ISite;
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
	private final int master_count;
	
	/**
	 * Constructor with user-specified .properties file and keyspace partitioner.
	 * @param file	.properties file name
	 * @param partitioner	{@link IPartitioner} for keyspace partition
	 */
	public ClientContextMultiMaster(String file, IPartitioner partitioner) {
		super(file);
		this.partitioner = partitioner;
		this.master_count = super.clusters.size();	// TODO check the initialization order
	}

	/**
	 * @return the master site located by the partition strategy specified by this {@link #partitioner}
	 */
	@Override
	public ISite getMasterResponsibleFor(CompoundKey ck) {
		int index = this.partitioner.locateSiteIndexFor(ck, this.master_count);
		return super.clusters.get(index).getMaster();
	}

	@Override
	public ISite getReadSite(CompoundKey ck) {
		return super.cached_read_site.orElseGet(() -> {
			int index = this.partitioner.locateSiteIndexFor(ck, this.master_count);
			ISite read_site = super.clusters.get(index).getSiteForRead(); 
			super.cached_read_site = Optional.of(read_site);
			return read_site;
		});
	}

}
