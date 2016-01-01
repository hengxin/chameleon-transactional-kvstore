package network.membership;

import java.util.List;

import context.Cluster;
import exception.rmi.SiteStubParseException;
import site.AbstractSite;
import site.ISite;

/**
 * A {@link MemberCluster} consists of a collection of {@link Member}s,
 * of which one is distinguished as the master.
 * <p> Used for {@link ClientMembership}.
 * @author hengxin
 * @date Created on Jan 1, 2016
 */
public class MemberCluster {

	private final int cno;
	private final Member master;
	private final List<Member> slaves;
	
	public MemberCluster(int cno, Member master, List<Member> slaves) {
		this.cno = cno;
		this.master = master;
		this.slaves = slaves;
	}
	
	/**
	 * Parse a {@link MemberCluster} into a {@link Cluster}.
	 * @param member_cluster	{@link MemberCluster} to parse
	 * @return		{@link Cluster} instance
	 * @throws SiteStubParseException	if an error occurs during parsing site stub
	 */
	public static Cluster parse(MemberCluster member_cluster) throws SiteStubParseException {
		ISite master_stub = AbstractSite.parseStub(member_cluster.master);
		List<ISite> slave_stubs = AbstractSite.parseStubs(member_cluster.slaves);
		
		return new Cluster(member_cluster.cno, master_stub, slave_stubs);
	}
	
}