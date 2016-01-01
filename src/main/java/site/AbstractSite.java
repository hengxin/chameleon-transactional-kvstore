package site;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

import context.IContext;
import exception.SiteException;
import exception.rmi.SiteStubParseException;
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
import rmi.IRMI;

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
public abstract class AbstractSite implements ISite, IRMI
{
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractSite.class);
	
	private final Member self;
	protected AbstractTable table;
	protected Optional<AbstractJMSParticipant> jmser = Optional.empty();
	protected final IContext context;
	
	public AbstractSite(IContext context)
	{
		this.context = context;
		this.self = context.self();
	}
	
	@Override
	public ITimestampedCell read(Row r, Column c)
	{
		return this.table.getTimestampedCell(r, c);
	}
	
	/**
	 * Export self for remote accesses via RMI.
	 */
	@Override
	public void export() throws SiteException
	{
		System.setProperty("java.rmi.server.hostname", this.self.getAddrIp());

		try
		{
			Remote remote = UnicastRemoteObject.exportObject(this, 0);	// port 0: chosen at runtime
			LocateRegistry.createRegistry(this.self.getRmiRegistryPort()).rebind(this.self.getRmiRegistryName(), remote);
			LOGGER.info("The site [{}] has successfully exported itself as [{}] for remote accesses.", this.self, remote);
		} catch (RemoteException re)
		{
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
	public void reclaim() throws SiteException
	{
		try
		{
			LocateRegistry.getRegistry(this.self.getAddrIp(), this.self.getRmiRegistryPort()).unbind(this.self.getRmiRegistryName());
		} catch (RemoteException | NotBoundException e)
		{
			throw new SiteException(String.format("Failed to reclaim self (%s) from remote access.", this.self), e.getCause());
		}
	}
	
	/**
	 * Locate the stub for the {@link Member}; Used later for RMI.
	 * 
	 * @param member An {@link Member} representing a site
	 * @return 
	 * 		A stub for a remote object, wrapped by {@link Optional}; 
	 * 		may be {@code Optional.empty()} if it fails to parse a stub from @param member.
	 * @throws SiteStubParseException	if an error occurs during parse
	 */
	public static ISite parseStub(Member member) throws SiteStubParseException
	{
		try
		{
			return (ISite) LocateRegistry.getRegistry(member.getAddrIp(), member.getRmiRegistryPort()).lookup(member.getRmiRegistryName());
		} catch (RemoteException | NotBoundException e)
		{
			throw new SiteStubParseException(String.format("Failed to locate the remote stub for [%s].", member), e.getCause());
		}
	}
	
	/**
	 * Locate the stubs for a list of {@link Member}s.
	 * 
	 * @param members 
	 * 		A list of {@link Member}s to be parsed.
	 * @return 
	 * 		A list of {@link ISite} stubs; 
	 */
	public static List<ISite> parseStubs(List<Member> members)
	{
		return members.parallelStream()
				.map(AbstractSite::parseStub)
				.collect(Collectors.toList());
	}
	
	public void registerAsJMSParticipant(AbstractJMSParticipant jmser)
	{
		this.jmser = Optional.of(jmser);
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.self)
				.toString();
	}
}
