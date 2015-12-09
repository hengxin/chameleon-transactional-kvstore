package site;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import context.IContext;
import exception.SiteException;
import jms.AbstractJMSParticipant;
import jms.master.JMSCommitLogPublisher;
import jms.slave.JMSCommitLogSubscriber;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;
import kvs.table.MasterTable;
import kvs.table.SlaveTable;
import master.IMaster;
import network.membership.Member;
import rmi.IRMI;
import slave.ISlave;

/**
 * An {@link AbstractSite} holds an {@link AbstractTable} 
 * and acts as an {@link AbstractJMSParticipant}.
 * 
 * <p> Specifically, an {@link IMaster} holds a {@link MasterTable}
 * and acts as an {@link JMSCommitLogPublisher}, while an {@link ISlave}
 * holds a {@link SlaveTable} and acts as an {@link JMSCommitLogSubscriber}.
 *  
 * @author hengxin
 * @date Created on 11-25-2015
 */
public abstract class AbstractSite implements ISite, IRMI
{
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractSite.class);
	
	protected AbstractTable table;
	protected AbstractJMSParticipant jmser;
	protected IContext context;
	
	public void registerAsJMSParticipant(AbstractJMSParticipant jmser)
	{
		this.jmser = jmser;
	}
	
	@Override
	public ITimestampedCell read(Row r, Column c)
	{
		return this.table.getTimestampedCell(r, c);
	}
	
	@Override
	public void export() throws SiteException
	{
		Member self = this.context.self();
		
		System.setProperty("java.rmi.server.hostname", self.getAddrIp());

		try
		{
			Remote remote = UnicastRemoteObject.exportObject(this, 0);	// port 0: chosen at runtime
			LocateRegistry.createRegistry(self.getRmiRegistryPort()).rebind(self.getRmiRegistryName(), remote);
			LOGGER.info("The site ({}) has exported itself as ({}) for remote accesses successfully.", self, remote);
		} catch (RemoteException re)
		{
			throw new SiteException(String.format("Failed to export self (%s) for remote accesses.", self), re.getCause());
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
		Member self = this.context.self();
		
		try
		{
			LocateRegistry.getRegistry(self.getRmiRegistryPort()).unbind(self.getRmiRegistryName());
		} catch (RemoteException | NotBoundException e)
		{
			throw new SiteException(String.format("Failed to reclaim self (%s) from remote access.", self), e.getCause());
		}
	}
}
