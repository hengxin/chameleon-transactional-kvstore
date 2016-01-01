package network.membership;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import exception.network.membership.MasterMemberParseException;
import exception.network.membership.MemberParseException;
import exception.network.membership.SlaveMemberParseException;

/**
 * A client needs to know all the masters and their individual slaves.
 * In this implementation, all of these membership information is loaded from this .properties file.
 * Each line is in the format of "master = a list of its slaves separated by commas"

 * An alternative approach (which I don't take now) is:
 * A client initially maintains a (full) list of all masters.
 * And it asks these masters for their individual slaves at runtime.
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class ClientMembership extends AbstractStaticMembership
{
	private List<MemberCluster> member_cluster_list;
	
	public ClientMembership(String file) throws MemberParseException
	{
		super(file);
	}

	@Override
	public void parseMembershipFromProp()
	{
		this.member_cluster_list = this.loadMemberClusters();
	}

	/**
	 * Load and parse {@link MemberCluster}s from .properties file which consists of 
	 * <p> cno = master, slave, slave, ...
	 * <p> cno = master, slave, slave, ...
	 * <p> ...
	 * @throws MasterMemberParseException
	 * @throws SlaveMemberParseException
	 */
	private List<MemberCluster> loadMemberClusters() throws MasterMemberParseException, SlaveMemberParseException
	{
		return super.prop.stringPropertyNames().parallelStream()
			.map( cluster_no_str -> {
				int cno = Integer.parseInt(cluster_no_str.trim());
				
				// cluster_str: master, slave, slave, ...
				String cluster_str = super.prop.getProperty(cluster_no_str).trim();
				
				// master and slaves in string format
				int sep = cluster_str.indexOf(',');
				String master_str = cluster_str.substring(0, sep);
				String slaves_str = cluster_str.substring(sep + 1).trim();
				
				// parse master
				Member master;
				try{
					master = Member.parseMember(master_str);
				} catch (MemberParseException mpe) {
					throw new MasterMemberParseException(mpe);
				}
				
				// parse slaves
				List<Member> slaves; 
				try{
					slaves = Member.parseMembers(slaves_str);
				} catch (MemberParseException mpe) {
					throw new SlaveMemberParseException(mpe);
				}

				return new MemberCluster(cno, master, slaves);
			}).collect(Collectors.toList());
	}
	
	public Member self()
	{
		// TODO identity of a client
		return null;
	}
	
	public Stream<MemberCluster> parallelStream()
	{
		return this.member_cluster_list.parallelStream();
	}
}
