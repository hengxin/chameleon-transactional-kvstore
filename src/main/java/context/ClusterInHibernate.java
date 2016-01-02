package context;

import java.util.Collections;
import java.util.List;

import com.sun.istack.Nullable;

import exception.network.membership.MasterMemberParseException;
import exception.network.membership.MemberParseException;
import exception.network.membership.SlaveMemberParseException;
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

	protected final int cno;
	protected final Member master;
	protected final List<Member> slaves;
	
	public ClusterInHibernate(int cno, @Nullable Member master, List<Member> slaves) {
		this.cno = cno;
		this.master = master;
		this.slaves = slaves;
	}
	
	/**
	 * Parse cluster in string format into an instance of {@link ClusterInHibernate}.
	 * @param cluster_no_str	string format of {@link #cno}
	 * @param cluster_str		string format of {@link #master} + {@link #slaves}, 
	 * 						separated by commas: master, slave, slave, ...
	 * @return	an instance of {@link ClusterInHibernate}
	 * @throws MasterMemberParseException	if it fails to parse the master of the cluster
	 * @throws SlaveMemberParseException	if it fails to parse some slave of the cluster
	 */
	public static ClusterInHibernate parse(String cluster_no_str, String cluster_str) {
				int cno = Integer.parseInt(cluster_no_str);
				
				// master and slaves in string format
				int sep = cluster_str.indexOf(',');
				String master_str = cluster_str.substring(0, sep);
				String slaves_str = cluster_str.substring(sep + 1).trim();
				
				// parse master
				Member master = null;
				try{
					master = Member.parseMember(master_str);
				} catch (MemberParseException mpe) {
					throw new MasterMemberParseException(mpe);
				}
				
				// parse slaves
				List<Member> slaves = Collections.emptyList(); 
				try{
					slaves = Member.parseMembers(slaves_str);
				} catch (MemberParseException mpe) {
					throw new SlaveMemberParseException(mpe);
				}

				return new ClusterInHibernate(cno, master, slaves);
	}
	
}