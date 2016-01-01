package client.context;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.transaction.RVSITransaction;
import context.Cluster;
import exception.network.membership.MasterMemberParseException;
import exception.rmi.SiteStubParseException;
import master.IMaster;
import network.membership.AbstractStaticMembership;
import network.membership.ClientMembership;
import network.membership.MemberCluster;
import site.ISite;
import slave.ISlave;

/**
 * Provides context for transaction processing at the client side, including
 * <p>
 * <ul>
 * <li> {@link #master_slaves_map}: a contact list of server nodes; 
 * 	organized as a map of {@link IMaster}s to their individual list of {@link ISlave}s.
 * <li> TODO {@link #tx}: the currently active {@link RVSITransaction}
 * <li> TODO {@link #newTx()} and {@link #endTx()}: life-cycle management of {@link #tx}. 
 * <li> TO BE IMPLEMENTED
 * </ul>
 * 
 * @author hengxin
 * @date Created on 10-28-2015
 */
public abstract class AbstractClientContext
{
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractClientContext.class);
	
	private AbstractStaticMembership client_membership; 
	
	protected final List<Cluster> clusters;
	
//	private RVSITransaction tx = null;
	
	/**
	 * Constructor with user-specified properties file.
	 * @param file 
	 * 		Path of the properties file.
	 * @throws SiteStubParseException	if an error occurs during paring site stub
	 */
	public AbstractClientContext(String file) throws SiteStubParseException
	{
		LOGGER.info("Using the properties file [{}] for [{}].", file, this.getClass().getSimpleName());

		try{
			this.client_membership = new ClientMembership(file);
		} catch (MasterMemberParseException mmpe) {
			LOGGER.error("Failed to create client context.", mmpe);
			System.exit(1);
		}

		this.clusters = this.loadRemoteClusters();
	}

	/**
	 * @return 	a list of {@link Cluster}s, <i>sorted</i> by their cluster no.
	 * @throws SiteStubParseException  if an error occurs during parsing site stub
	 * <p> FIXME To be fault-tolerant:
	 * 		If a master is not available, then all of its slaves are ignored;
	 * 		If a master is available, then some of its slaves may be ignored if unavailable.
	 */
	protected List<Cluster> loadRemoteClusters() throws SiteStubParseException
	{
		return ((ClientMembership) this.client_membership).parallelStream()
				.map(MemberCluster::parse)
				.sorted(Cluster.CLUSTER_NO_COMPARATOR)
				.collect(Collectors.toList());
	}
	
	public abstract ISite getReadSite();
}