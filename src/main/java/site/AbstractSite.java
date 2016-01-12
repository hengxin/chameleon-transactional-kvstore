package site;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

import context.IContext;
import exception.rmi.RMIRegistryException;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;
import network.membership.Member;
import rmi.IRMI;

/**
 * An {@link AbstractSite} holds an {@link AbstractTable}, upon which
 * it provides remotely available {@code get/put} operations 
 * (by implementing {@link ISite}).
 * An {@link AbstractSite} exports itself for RMI calls by implementing {@link IRMI}.
 * @author hengxin
 * @date Created on 11-25-2015
 */
public abstract class AbstractSite implements ISite, IRMI {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractSite.class);
	
	private final Member self;
	protected final IContext context;
	protected AbstractTable table;
	
	public AbstractSite(IContext context) {
		this.context = context;
		this.self = context.self();
	}
	
	/**
	 * FIXME Is it appropriate for {@link #get(Row, Column)} here?
	 */
	@Override
	public ITimestampedCell get(Row r, Column c) {
		return this.table.getTimestampedCell(r, c);
	}
	
	/**
	 * FIXME implementation? Is it appropriate for {@link #put(Row, Column, ITimestampedCell)} here?
	 */
	@Override
	public boolean put(Row r, Column c, ITimestampedCell ts_cell) {
		return false;
	}

	/**
	 * Export self for remote accesses via RMI.
	 */
	@Override
	public void export() {
		System.setProperty("java.rmi.server.hostname", this.self.getAddrIp());

		try {
			Remote remote = UnicastRemoteObject.exportObject(this, 0);	// port 0: chosen at runtime
			LocateRegistry.createRegistry(this.self.getRmiRegistryPort()).rebind(this.self.getRmiRegistryName(), remote);
			LOGGER.info("The site [{}] has successfully exported itself as [{}] for remote accesses.", this.self, remote);
		} catch (RemoteException re) {
			throw new RMIRegistryException(String.format("Failed to export self [%s] for remote accesses.", self), re.getCause());
		}
	}
	
	/**
	 * @implNote 
	 * 	@deprecated FIXME Currently this implementation is buggy. Don't call it in your code.
	 */
	@Deprecated
	@Override
	public void reclaim() {
		try {
			LocateRegistry.getRegistry(this.self.getAddrIp(), this.self.getRmiRegistryPort()).unbind(this.self.getRmiRegistryName());
		} catch (RemoteException | NotBoundException e) {
			throw new RMIRegistryException(String.format("Failed to reclaim self (%s) from remote access.", this.self), e.getCause());
		}
	}
	
	/**
	 * Locate the stub for the {@link Member}; used later for RMI invocation.
	 * @param member an {@link Member} representing a site
	 * @return 	an {@link Optional}-wrapped remote stub of {@link ISite}; 
	 * 	it could be {@code Optional.empty()} if an error occurs during RMI localization.
	 */
	public static Optional<ISite> locateRMISite(Member member) {
		try {
			ISite site = (ISite) LocateRegistry.getRegistry(member.getAddrIp(), 
															member.getRmiRegistryPort())
											   .lookup(member.getRmiRegistryName());
			return Optional.of(site);
		} catch (RemoteException | NotBoundException e) {
			LOGGER.warn("Cannot locate [{}] via RMI.", member);
			return Optional.empty();
		}
	}
	
	/**
	 * Locate the stubs for a list of {@link Member}; used later for RMI invocation.
	 * @param members a list of {@link Member}s to be located.
	 * @return 	a list of {@link ISite} stubs
	 * @implNote Note that only the {@link ISite}s that can be located 
	 * 	via RMI are returned; others are ignored. Therefore, the return list may be empty.
	 */
	public static List<ISite> locateRMISites(List<Member> members) {
		return members.stream()
				.map(AbstractSite::locateRMISite)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(this.self)
				.toString();
	}

}
