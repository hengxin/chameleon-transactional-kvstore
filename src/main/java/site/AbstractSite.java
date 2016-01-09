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
import exception.SiteException;
import exception.rmi.RMIRegistryException;
import jms.AbstractJMSParticipant;
import jms.master.JMSCommitLogPublisher;
import jms.slave.JMSCommitLogSubscriber;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;
import kvs.table.MasterTable;
import kvs.table.SlaveTable;
import network.membership.Member;
import rmi.IRemoteSite;

/**
 * An {@link AbstractSite} holds an {@link AbstractTable} 
 * and acts as an {@link AbstractJMSParticipant}.
 * <p> 
 * Specifically, a master site holds a {@link MasterTable}
 * and acts as an {@link JMSCommitLogPublisher}, while a slave site
 * holds a {@link SlaveTable} and acts as an {@link JMSCommitLogSubscriber}.
 *  
 * @author hengxin
 * @date Created on 11-25-2015
 */
public abstract class AbstractSite implements IDataProvider, IRemoteSite {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractSite.class);
	
	private final Member self;
	protected AbstractTable table;
	protected Optional<AbstractJMSParticipant> jmser = Optional.empty();
	protected final IContext context;
	
	public AbstractSite(IContext context) {
		this.context = context;
		this.self = context.self();
	}
	
	/**
	 * FIXME Is it appropriate for {@link #read(Row, Column)} here?
	 */
	@Override
	public ITimestampedCell read(Row r, Column c) {
		return this.table.getTimestampedCell(r, c);
	}
	
	/**
	 * FIXME implementation? Is it appropriate for {@link #write(Row, Column, ITimestampedCell)} here?
	 */
	@Override
	public boolean write(Row r, Column c, ITimestampedCell ts_cell) throws RemoteException {
		return false;
	}

	/**
	 * Export self for remote accesses via RMI.
	 */
	@Override
	public void export() throws SiteException {
		System.setProperty("java.rmi.server.hostname", this.self.getAddrIp());

		try {
			Remote remote = UnicastRemoteObject.exportObject(this, 0);	// port 0: chosen at runtime
			LocateRegistry.createRegistry(this.self.getRmiRegistryPort()).rebind(this.self.getRmiRegistryName(), remote);
			LOGGER.info("The site [{}] has successfully exported itself as [{}] for remote accesses.", this.self, remote);
		} catch (RemoteException re) {
			throw new SiteException(String.format("Failed to export self [%s] for remote accesses.", self), re.getCause());
		}
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @implNote
	 * 		FIXME Currently this implementation is buggy. Don't call it in your code.
	 */
	@Override
	public void reclaim() throws SiteException {
		try {
			LocateRegistry.getRegistry(this.self.getAddrIp(), this.self.getRmiRegistryPort()).unbind(this.self.getRmiRegistryName());
		} catch (RemoteException | NotBoundException e) {
			throw new SiteException(String.format("Failed to reclaim self (%s) from remote access.", this.self), e.getCause());
		}
	}
	
	/**
	 * Locate the stub for the {@link Member}; used later for RMI invocation.
	 * 
	 * @param member an {@link Member} representing a site
	 * @return 	a remote stub of {@link IRemoteSite}
	 * @throws RMIRegistryException		if an error occurs during RMI locating
	 */
	public static IRemoteSite locateRMISite(Member member) {
		try {
			return (IRemoteSite) LocateRegistry.getRegistry(member.getAddrIp(), member.getRmiRegistryPort()).lookup(member.getRmiRegistryName());
		} catch (RemoteException | NotBoundException e) {
			throw new RMIRegistryException(member, e);
		}
	}
	
	/**
	 * Locate the stubs for a list of {@link Member}. used later for RMI invocation.
	 * @param members a list of {@link Member}s to be parsed.
	 * @return 	a list of {@link IRemoteSite} stubs; 
	 * @throws	RMIRegistryException 	if an error occurs in locating remote stub for some site
	 */
	public static List<IRemoteSite> locateRMISites(List<Member> members) {
		return members.parallelStream()
				.map(AbstractSite::locateRMISite)
				.collect(Collectors.toList());
	}
	
	public void registerAsJMSParticipant(AbstractJMSParticipant jmser) {
		this.jmser = Optional.of(jmser);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(this.self)
				.toString();
	}

}
