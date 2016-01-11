package context;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import network.membership.ClientMembership;
import network.membership.Member;

/**
 * A {@link ClusterInHibernate} consists of a collection of {@link Member}s,
 * of which one is distinguished as the master.
 * <p> Used for {@link ClientMembership}.
 * @author hengxin
 * @date Created on Jan 1, 2016
 */
public class ClusterInHibernate {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClusterInHibernate.class);

	/** globally unique cluster no **/
	protected final int cno;
	@Nonnull protected final Member master;
	@Nonnull protected final List<Member> slaves;
	
	public ClusterInHibernate(int cno, @Nonnull Member master, List<Member> slaves) {
		this.cno = cno;
		this.master = master;
		this.slaves = slaves;
	}
	
	/**
	 * Parse cluster in string format into an instance of {@link ClusterInHibernate}.
	 * @param cluster_no_str	string format of {@link #cno}
	 * @param cluster_str		string format of {@link #master} + {@link #slaves}, 
	 * 						separated by commas: master, slave, slave, ...
	 * @return	an instance of {@link ClusterInHibernate}; If the master of this cluster
	 * 	cannot be parsed, the system exits.
	 */
	public static ClusterInHibernate parse(String cluster_no_str, String cluster_str) {
				int cno = Integer.parseInt(cluster_no_str);
				
				// master and slaves in string format
				int sep = cluster_str.indexOf(',');
				String master_str = cluster_str.substring(0, sep);
				String slaves_str = cluster_str.substring(sep + 1).trim();
				
				// parse master
				Optional<Member> master_opt = Member.parseMember(master_str);
				if (! master_opt.isPresent()) {
					LOGGER.error("Cannot parse the master [{}]", master_str);
					System.exit(1);	// fail fast
				}
				
				return new ClusterInHibernate(cno, master_opt.get(), 
												Member.parseMembers(slaves_str));
	}
	
}