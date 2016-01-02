package client.context;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.transaction.RVSITransaction;
import context.ClusterActive;
import exception.network.membership.MasterMemberParseException;
import exception.network.membership.MemberParseException;
import exception.rmi.RMIRegistryException;
import exception.rmi.RMIRegistryForMasterException;
import network.membership.AbstractStaticMembership;
import network.membership.ClientMembership;
import site.ISite;

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
	
	private AbstractStaticMembership client_membership; 
	
	protected List<ClusterActive> clusters;
	
//	private RVSITransaction tx = null;
	
	/**
	 * Constructor with user-specified properties file.
	 * <p> If some master site cannot be parsed from .properties file 
	 * or located via RMI, the whole system exits immediately.
	 * Slaves
	 * @param file		path of the properties file.
	 * @throws RMIRegistryException	if an error occurs during paring site stub
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
		} catch (RMIRegistryForMasterException rrfme) {
			LOGGER.error("Failed to create client context.", rrfme);
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
		return ((ClientMembership) this.client_membership).parallelStream()
				.map(ClusterActive::activate)
				.sorted(ClusterActive.CLUSTER_NO_COMPARATOR)
				.collect(Collectors.toList());
	}
	
	public abstract ISite getReadSite();
}