package context;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.google.common.base.MoreObjects;

import exception.rmi.RMIRegistryException;
import exception.rmi.RMIRegistryForMasterException;
import exception.rmi.RMIRegistryForSlaveException;
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
	/** globally unique cluster no. **/
	private final int cno;
	private final ISite master;
	private final List<ISite> slaves;
	
	/**
	 * Compare two {@link ClusterActive}s by {@link ClusterActive#cno}.
	 * @author hengxin
	 * @date Created on Jan 1, 2016
	 */
	private static class ClusterNoCmp implements Comparator<ClusterActive> {

		@Override
		public int compare(ClusterActive c1, ClusterActive c2) {
			return c1.cno - c2.cno;
		}

	}

	/** comparator for sorting {@link ClusterActive}s by {@link ClusterActive#cno} */
	public static final Comparator<ClusterActive> CLUSTER_NO_COMPARATOR = new ClusterNoCmp();

	/**
	 * @param cno	globally unique cluster no.
	 * @param master	master site of this cluster
	 * @param slaves	slaves of this cluster
	 */
	public ClusterActive(int cno, ISite master, List<ISite> slaves) {
		this.cno = cno;
		this.master = master;
		this.slaves = slaves;
	}
	
	/**
	 * Activate a {@link ClusterInHibernate} into a {@link ClusterActive}.
	 * @param hibernate_cluster	{@link ClusterInHibernate} to parse
	 * @return		an instance of {@link ClusterActive} 
	 * @throws RMIRegistryForMasterException	if an error occurs in locating remote stub for the master
	 * @throws RMIRegistryForSlaveException		if an error occurs in locating remote stub for some slave
	 */
	public static ClusterActive activate(ClusterInHibernate hibernate_cluster) {
		ISite master_stub = null;
		try{
			master_stub = AbstractSite.locateRMISite(hibernate_cluster.master);
		} catch (RMIRegistryException rre) {
			throw new RMIRegistryForMasterException(rre);
		}

		List<ISite> slave_stubs = Collections.emptyList();
		try{
			slave_stubs = AbstractSite.locateRMISites(hibernate_cluster.slaves);
		} catch (RMIRegistryException rre) {
			throw new RMIRegistryForSlaveException(rre);
		}
		
		return new ClusterActive(hibernate_cluster.cno, master_stub, slave_stubs);
	}
	
	public ISite getMaster() {
		return this.master;
	}
	
	/**
	 * Return a site for read. It perfers a slave site. If no slaves are available, it returns the master.
	 * @return	an {ISite} in this cluster
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
