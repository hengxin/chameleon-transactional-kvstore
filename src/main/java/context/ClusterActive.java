package context;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

import rmi.IRMI;
import site.AbstractSite;
import site.ISite;

/**
 * A {@link ClusterActive} consists of a master and a collection of its slaves,
 * upon which RMI can be invoked.
 * Each cluster has a globally unique number, upon which a {@link Comparator} is provided. 
 * @author hengxin
 * @date Created on Jan 1, 2016
 */
public final class ClusterActive {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterActive.class);
	
	/** globally unique cluster no. **/
	private final int cno;
	@Nonnull private final ISite master;
	@Nonnull private final List<ISite> slaves;
	
	/** {@link Comparator} for {@link ClusterActive} by their {@link #cno} **/
	public static final Comparator<ClusterActive> COMPARATOR_BY_CLUSTER_NO = Comparator.comparingInt(ClusterActive::getCno);

	/**
	 * @param cno		<i>globally unique</i> cluster no
	 * @param master	master site of this cluster; it cannot be {@code null}.
	 * @param slaves	a list of slaves of this cluster
	 */
	public ClusterActive(int cno, @Nonnull ISite master, @Nonnull List<ISite> slaves) {
		this.cno = cno;
		this.master = master;
		this.slaves = slaves;
	}
	
	/**
	 * Activates a {@link ClusterInHibernate} into a {@link ClusterActive}.
	 * @param hibernate_cluster	a {@link ClusterInHibernate} to activate
	 * @return	an instance of {@link ClusterActive} if it can be activated; 
	 * 	otherwise, the system exits.
	 */
	public static ClusterActive activate(ClusterInHibernate hibernate_cluster) {
		Optional<ISite> master_stub = AbstractSite.locateRMISite(hibernate_cluster.master);
		if(! master_stub.isPresent()) {
			LOGGER.error("Cannot activate this cluster [{}] because the master [{}] cannot be activated.", hibernate_cluster, hibernate_cluster.master);
			System.exit(1);	// fail fast
		}
		
		return new ClusterActive(hibernate_cluster.cno, master_stub.get(), 
									AbstractSite.locateRMISites(hibernate_cluster.slaves));
	}
	
	public int getCno() {
		return this.cno;
	}
	
	public ISite getMaster() {
		return this.master;
	}
	
	/**
	 * Return a site for read. It perfers a slave site. If no slaves are available, it returns the master.
	 * @return	an {@link IRMI} in this cluster
	 */
	public ISite getSiteForRead() {
		return this.slaves.isEmpty() ? this.getMaster() : this.getRandomSlave();
	}

	/**
	 * @return	a random slave site in this cluster
	 */
	private ISite getRandomSlave() {
		return slaves.get(new Random().nextInt(slaves.size()));
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("cno", this.cno)
				.addValue(this.master)
				.addValue(this.slaves)
				.toString();
	}
}
