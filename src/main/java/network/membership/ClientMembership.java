package network.membership;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import context.ClusterInHibernate;

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
public final class ClientMembership extends AbstractStaticMembership {

	private List<ClusterInHibernate> hibernate_cluster_list;
	
	public ClientMembership(@Nonnull String file) {
		super(file);
	}

	/**
	 * Load and parse {@link ClusterInHibernate}s from {@link super#prop} which is in form of 
	 * <p> cno = master, slave, slave, ...
	 * <p> cno = master, slave, slave, ...
	 * <p> ...
	 * @implNote	The system exits if some {@link ClusterInHibernate} cannot be parsed successfully.
	 */
	@Override
	public void parseMembershipFromProp() {
		this.hibernate_cluster_list = 
				super.prop.stringPropertyNames()
						  .stream()
						  .map(cluster_no_str -> 
						  		ClusterInHibernate.parse(cluster_no_str, super.prop.getProperty(cluster_no_str)))
						  .collect(Collectors.toList());
	}

	public Member self() {
		// TODO identity of a client
		return null;
	}
	
	public Stream<ClusterInHibernate> stream() {
		return this.hibernate_cluster_list.stream();
	}
}
