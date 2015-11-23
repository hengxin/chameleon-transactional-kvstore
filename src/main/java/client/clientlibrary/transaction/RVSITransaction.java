/**
 * 
 */
package client.clientlibrary.transaction;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

import client.clientlibrary.rvsi.rvsimanager.RVSIManager;
import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.rvsi.rvsispec.SVSpecification;
import client.communication.ClientContacts;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date 10-27-2015
 * 
 * Implement our RVSI transaction which is enhanced by rvsi specification.
 */
public class RVSITransaction implements ITransaction
{
	private Timestamp sts = Timestamp.TIMESTAMP_INIT;	// start-timestamp
	private Timestamp cts = Timestamp.TIMESTAMP_INIT;	// commit-timestamp

	private BufferedUpdates buffered_updates = new BufferedUpdates();	// to buffer write operations
	private QueryResults query_results = new QueryResults();			// to store query results
	
	private RVSIManager rvsi_manager = new RVSIManager();				// to deal with {@link AbstractRVSISpecification}-related things

	/* 
	 * @see client.clientlibrary.Transaction#begin()
	 */
	@Override
	public boolean begin()
	{
		try
		{
			this.sts = ClientContacts.INSTANCE.getRemote_master().start();
		} catch (InterruptedException | ExecutionException | RemoteException e)
		{
			// TODO: to remove
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/* 
	 * @see client.clientlibrary.Transaction#read(kvs.table.Row, kvs.table.Column)
	 */
	@Override
	public boolean read(Row r, Column c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* 
	 * @see client.clientlibrary.Transaction#write(kvs.table.Row, kvs.table.Column, kvs.table.Cell)
	 */
	@Override
	public boolean write(Row r, Column c, Cell data)
	{
		this.buffered_updates.intoBuffer(r, c, data);
		return true;
	}

	/* 
	 * @see client.clientlibrary.Transaction#end()
	 */
	@Override
	public boolean end()
	{
		VersionConstraintManager vc_manager = this.generateVersionConstraintManager();
		
		try
		{
			boolean success = ClientContacts.INSTANCE.getRemote_master().commit(this.sts, this.buffered_updates, vc_manager);
			if(! success)
			{
				// TODO restart the transaction???
			}
		} catch (RemoteException re)
		{
			// TODO: to remove
			re.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Collect {@link AbstractRVSISpecification} whose type could be {@link BVSpecification}, 
	 * {@link FVSpecification}, or {@link SVSpecification}.
	 * 
	 * @param rvsi_spec an {@link AbstractRVSISpecification}
	 */
	public void collectRVSISpecification(AbstractRVSISpecification rvsi_spec)
	{
		this.rvsi_manager.collectRVSISpecification(rvsi_spec);
	}
	
	public VersionConstraintManager generateVersionConstraintManager()
	{
		return this.rvsi_manager.generateVersionConstraintManager(this);
	}
	
	public Timestamp getSts()
	{
		return this.sts;
	}
	
	public QueryResults getQueryResults()
	{
		return this.query_results;
	}
}
