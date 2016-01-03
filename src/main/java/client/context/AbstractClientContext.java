package client.context;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.transaction.RVSITransaction;
import context.ClusterActive;
import exception.network.membership.MasterMemberParseException;
import exception.network.membership.MemberParseException;
import exception.rmi.RMIRegistryException;
import exception.rmi.RMIRegistryForMasterException;
import exception.rmi.RMIRegistryForSlaveException;
import kvs.compound.CompoundKey;
import network.membership.AbstractStaticMembership;
import network.membership.ClientMembership;
import site.ISite;
import twopc.partitioning.IPartitioner;

/**
 * Provides context for transaction processing at the client side, including
 * <p><ul>
 * <li> {@link #clusters}: a list of {@link ClusterActive}
 * <li> TODO {@link #tx}: the currently active {@link RVSITransaction}
 * <li> TODO {@link #newTx()} and {@link #endTx()}: life-cycle management of {@link #tx}. 
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
	 * Slaves
	 * @param file		path of the properties file.
	 */
	public AbstractClientContext(String file) {
		LOGGER.info("Using the properties file [{}] for [{}].", file, this.getClass().getSimpleName());

		try{
			this.client_membership = new ClientMembership(file);
		} catch (MasterMemberParseException mmpe) {
			LOGGER.error("Failed to create client context.", mmpe);
			System.exit(1);
		} catch (MemberParseException mpe) {
			LOGGER.warn("Some slave sites cannot be parsed.", mpe);
		}

		try{
			this.clusters = this.activateClusters();
			this.master_count = this.clusters.size();
		} catch (RMIRegistryForMasterException rrfme) {
			LOGGER.error("Failed to create client context.", rrfme);
			System.exit(1);
		} catch (RMIRegistryException rre) {
			LOGGER.warn("Some slave sites cannot be located via RMI", rre);
		}
	}

	/**
	 * Activate clusters for later RMI invocation.
	 * @return 	a list of {@link ClusterActive}s, <i>sorted</i> by their cluster no.
	 * @throws RMIRegistryForMasterException	if an error occurs in locating remote stub for some master site
	 * @throws RMIRegistryForSlaveException		if an error occurs in locating remote stub for some slave site
	 */
	protected List<ClusterActive> activateClusters() {
		return ((ClientMembership) this.client_membership).stream()
				.map(ClusterActive::activate)
				.sorted(ClusterActive.CLUSTER_NO_COMPARATOR)
				.collect(Collectors.toList());
	}
	
	/**
	 * Return a master site who holds value(s) of the specified key.
	 * @param ck	{@link CompoundKey} key
	 * @return		the master {@link ISite} holding @param ck
	 * @implNote 	This implementation requires and utilizes the {@link #partitioner}
	 * 	specified by subclasses of this {@link AbstractClientContext}. If you don't want
	 *  to rely on {@link Partitioner}, you can override this method.
	 */
	public ISite getMasterResponsibleFor(CompoundKey ck) {
		int index = this.partitioner.locateSiteIndexFor(ck, this.master_count);
		return this.clusters.get(index).getMaster();
	}


	/**
	 * Return a site who holds value(s) of the specified key.
	 * @param ck	{@link CompoundKey} key
	 * @return		an {@link ISite} holding @param ck
	 * @implNote	In principle, the client is free to contact <i>any</i> site to read.
	 * 	In this particular implementation, it prefers an already cached slave. 
	 * 
	 *  <p>This implementation requires and utilizes the {@link #partitioner} 
	 * 	specified by subclasses of this {@link AbstractClientContext}. If you don't want
	 *  to rely on {@link Partitioner}, you can override this method.
	 */
	public ISite getReadSite(CompoundKey ck) {
		return this.cached_read_site.orElseGet(() -> {
			int index = this.partitioner.locateSiteIndexFor(ck, this.master_count);
			ISite read_site = this.clusters.get(index).getSiteForRead(); 
			this.cached_read_site = Optional.of(read_site);
			return read_site;
		});
	}

}