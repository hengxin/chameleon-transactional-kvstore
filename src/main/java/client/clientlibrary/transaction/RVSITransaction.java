package client.clientlibrary.transaction;

import java.rmi.RemoteException;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.RVSISpecificationManager;
import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.rvsi.rvsispec.SVSpecification;
import client.communication.ClientContext;
import client.communication.ClientContextSingleMaster;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;

/**
 * Implement our RVSI transactions which are allowed to specify {@link AbstractRVSISpecification}.
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 */
public class RVSITransaction implements ITransaction
{
	private final static Logger LOGGER = LoggerFactory.getLogger(RVSITransaction.class);
	
	private final ClientContext context;
	
	private Timestamp sts = Timestamp.TIMESTAMP_INIT_ZERO;	// start-timestamp
	private Timestamp cts = Timestamp.TIMESTAMP_INIT_ZERO;	// commit-timestamp

	private final BufferedUpdates buffered_updates = new BufferedUpdates();	
	private final QueryResults query_results = new QueryResults();
	
	private final RVSISpecificationManager rvsi_manager = new RVSISpecificationManager();

	public RVSITransaction(ClientContext context)
	{
		this.context = context;
	}
	
	/* 
	 * To begin a transaction, the client contacts <i>the</i> master to 
	 * acquire a globally unique start timestamp.
	 */
	@Override
	public boolean begin()
	{
		try
		{
			this.sts = ((ClientContextSingleMaster) context).getMaster().start();
		} catch (InterruptedException | ExecutionException | RemoteException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * In principle, the client is free to contact <i>any</i> site to read.
	 * In this particular implementation, it prefers a nearby slave. 
	 */
	@Override
	public boolean read(Row r, Column c)
	{
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 
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
		ToCommitTransaction tx = new ToCommitTransaction(this.sts, this.buffered_updates);
		
		try
		{
			boolean success = ((ClientContextSingleMaster) context).getMaster().commit(tx, vc_manager);
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
