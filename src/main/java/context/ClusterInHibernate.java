package context;

import java.util.List;

import exception.rmi.SiteStubParseException;
import network.membership.ClientMembership;
import network.membership.Member;
import site.AbstractSite;
import site.ISite;

/**
 * A {@link ClusterInHibernate} consists of a collection of {@link Member}s,
 * of which one is distinguished as the master.
 * <p> Used for {@link ClientMembership}.
 * @author hengxin
 * @date Created on Jan 1, 2016
 */
public class ClusterInHibernate {

	private final int cno;
	private final Member master;
	private final List<Member> slaves;
	
	public ClusterInHibernate(int cno, Member master, List<Member> slaves) {
		this.cno = cno;
		this.master = master;
		this.slaves = slaves;
	}
	
	/**
	 * Parse a {@link ClusterInHibernate} into a {@link ClusterActive}.
	 * @param member_cluster	{@link ClusterInHibernate} to parse
	 * @return		{@link ClusterActive} instance
	 * @throws SiteStubParseException	if an error occurs during parsing site stub
	 */
	public static ClusterActive parse(ClusterInHibernate member_cluster) throws SiteStubParseException {
		ISite master_stub = AbstractSite.parseStub(member_cluster.master);
		List<ISite> slave_stubs = AbstractSite.parseStubs(member_cluster.slaves);
		
		return new ClusterActive(member_cluster.cno, master_stub, slave_stubs);
	}
	
}