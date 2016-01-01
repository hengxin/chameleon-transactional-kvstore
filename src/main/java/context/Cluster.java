package context;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.google.common.base.MoreObjects;

import site.ISite;

/**
 * A cluster consists of a master and a collection of its slaves.
 * Each cluster has a globally unique number, upon which a {@link Comparator}
 * is provided. 
 * @author hengxin
 * @date Created on Jan 1, 2016
 */
public final class Cluster {
	/** globally unique cluster no. **/
	private final int cno;
	private final ISite master;
	private final List<ISite> slaves;
	
	/**
	 * Compare two {@link Cluster}s by {@link Cluster#cno}.
	 * @author hengxin
	 * @date Created on Jan 1, 2016
	 */
	private static class ClusterNoCmp implements Comparator<Cluster> {

		@Override
		public int compare(Cluster c1, Cluster c2) {
			return c1.cno - c2.cno;
		}

	}

	/** comparator for sorting {@link Cluster}s by {@link Cluster#cno} */
	public static final Comparator<Cluster> CLUSTER_NO_COMPARATOR = new ClusterNoCmp();

	/**
	 * @param cno	globally unique cluster no.
	 * @param master	master site of this cluster
	 * @param slaves	slaves of this cluster
	 */
	public Cluster(int cno, ISite master, List<ISite> slaves) {
		this.cno = cno;
		this.master = master;
		this.slaves = slaves;
	}
	
	public ISite getMaster() {
		return this.master;
	}
	
	/**
	 * @return	a random slave site in this cluster
	 */
	public ISite getRandomSlave() {
		return slaves.get(new Random().nextInt(slaves.size()));
	}

	/**
	 * @return	{@code true} if there is no slaves in this cluster; {@code false}, otherwise.
	 */
	public boolean hasNoSlaves() {
		return this.slaves.isEmpty();
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
