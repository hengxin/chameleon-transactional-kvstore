package membership.site;

import javax.annotation.Nonnull;

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
@Deprecated
public final class ClientMembership extends AbstractStaticMembership {

//	private List<ClusterInHibernate> hibernate_cluster_list;
	private ISiteMembership membership;

	public ClientMembership(@Nonnull String file) {
		super(file);
	}

//	/**
//	 * Load and parseReplGrps {@link ClusterInHibernate}s from {@link super#prop} which is in form of
//	 * <p> cno = master, slave, slave, ...
//	 * <p> cno = master, slave, slave, ...
//	 * <p> ...
//	 * @implNote	The system exits if some {@link ClusterInHibernate} cannot be parsed successfully.
//	 */
//	@Override
//	public void parseMembershipFromProp() {
//		this.hibernate_cluster_list =
//				super.prop.stringPropertyNames().stream()
//						  .map(cluster_no_str ->
//						  		ClusterInHibernate.parseReplGrps(cluster_no_str, super.prop.getProperty(cluster_no_str)))
//						  .collect(Collectors.toList());
//	}

	public Member self() {
		// TODO identity of a client
		return null;
	}
	
}
