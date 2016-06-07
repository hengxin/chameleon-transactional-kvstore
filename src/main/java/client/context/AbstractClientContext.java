package client.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.transaction.RVSITransaction;
import client.clientlibrary.transaction.ToCommitTransaction;
import context.ClusterActive;
import kvs.compound.CompoundKey;
import network.membership.AbstractStaticMembership;
import network.membership.ClientMembership;
import rmi.IRMI;
import site.ISite;

/**
 * Provides context for transaction processing at the client side, including
 * <p><ul>
 * <li> {@link #clusters}: a list of {@link ClusterActive}
 * <li> TODO {link #tx}: the currently active {@link RVSITransaction}
 * <li> TODO {link #newTx()} and {link #endTx()}: life-cycle management of {link #tx}.
 * <li> TO BE IMPLEMENTED
 * </ul>
 * 
 * @author hengxin
 * @date Created on 10-28-2015
 */
public abstract class AbstractClientContext {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractClientContext.class);

	protected final static String DEFAULT_CLIENT_PROPERTIES_FILE = "client/membership-client.properties";
	
	private AbstractStaticMembership client_membership; 
	
	protected List<ClusterActive> clusters;
	protected int master_count;

	protected IPartitioner partitioner;
	protected Optional<ISite> cached_read_site = Optional.empty();	
	
//	private RVSITransaction tx = null;
	
	/**
	 * Constructor with user-specified properties file.
	 * <p> If some master site cannot be parsed from .properties file 
	 * or located via RMI, the whole system exits immediately.
	 * @param file		path of the properties file; it cannot be {@code null}.
	 */
	public AbstractClientContext(@Nonnull String file) {
		LOGGER.info("Using the properties file [{}] for [{}].", file, this.getClass().getSimpleName());

		this.client_membership = new ClientMembership(file);

		this.clusters = this.activateClusters();
		this.master_count = this.clusters.size();
	}

	/**
	 * Activates clusters for later RMI invocation.
	 * @return 	a list of {@link ClusterActive}s, <i>sorted</i> by their cluster no.
	 * @implNote	If some {@link ClusterActive} cannot be activated, the system exits.
	 * @See {@link ClusterActive#activate(context.ClusterInHibernate)}
	 */
	protected List<ClusterActive> activateClusters() {
		return ((ClientMembership) this.client_membership).stream()
				.map(ClusterActive::activate)
				.sorted(ClusterActive.COMPARATOR_BY_CLUSTER_NO)
				.collect(Collectors.toList());
	}
	
	/**
	 * Partitions a {@link ToCommitTransaction} into multiple sub-{@link ToCommitTransaction}s,
	 * each of which will be dispatched to an {@link IRMI} responsible for it.
	 * <p>It returns a map from an {@link IRMI} to the sub-{@link ToCommitTransaction} it is responsible for. 
	 * @param tx	{@link ToCommitTransaction} to be partitioned
	 * @return a map from an {@link IRMI} to the sub-{@link ToCommitTransaction} it is responsible for 
	 */
	public Map<ISite, ToCommitTransaction> partition(ToCommitTransaction tx) {
		Map<Integer, ToCommitTransaction> index_items_map = tx.partition(partitioner, master_count);

		return index_items_map.keySet().stream()
                .collect(Collectors.toMap(
                        index -> getMaster(index.intValue()),
                        index_items_map::get
                ));
	}
	
	/**
	 * Return a master site who holds value(s) of the specified key.
	 * @param ck	{@link CompoundKey} key
	 * @return		the master {@link IRMI} holding @param ck
	 * @implNote 	This implementation requires and utilizes the {@link #partitioner}
	 * 	specified by subclasses of this {@link AbstractClientContext}. If you don't want
	 *  to rely on {@link IPartitioner}, you can override this method.
	 */
	public ISite getMasterFor(CompoundKey ck) {
		int index = partitioner.locateSiteIndexFor(ck, this.master_count);
		return this.getMaster(index); 
	}

	/**
	 * Return a site who holds value(s) of the specified key.
	 * @param ck	{@link CompoundKey} key
	 * @return		an {@link IRMI} holding @param ck
	 * @implNote	In principle, the client is free to contact <i>any</i> site to read.
	 * 	In this particular implementation, it prefers an already cached slave. 
	 * 
	 *  <p>This implementation requires and utilizes the {@link #partitioner} 
	 * 	specified by subclasses of this {@link AbstractClientContext}. If you don't want
	 *  to rely on {@link IPartitioner}, you can override this method.
	 */
	public ISite getReadSite(CompoundKey ck) {
		return this.cached_read_site.orElseGet(() -> {
			int index = this.partitioner.locateSiteIndexFor(ck, this.master_count);
			ISite read_site = this.clusters.get(index).getSiteForRead(); 
			this.cached_read_site = Optional.of(read_site);
			return read_site;
		});
	}
	
	/**
	 * Returns the master site of the {@link ClusterActive} with specified cluster_no.
	 * @param cno	specified cluster_no
	 * @return	the master site of the {@link ClusterActive} with @param cno 
	 */
	private ISite getMaster(int cno) {
		return this.clusters.get(cno).getMaster();
	}

}