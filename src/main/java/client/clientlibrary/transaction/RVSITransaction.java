package client.clientlibrary.transaction;

import java.net.NoRouteToHostException;
import java.rmi.ConnectIOException;
import java.rmi.RemoteException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.rvsi.rvsimanager.RVSISpecificationManager;
import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.rvsi.rvsispec.SVSpecification;
import client.context.AbstractClientContext;
import client.context.ClientContextSingleMaster;
import exception.transaction.TransactionEndException;
import exception.transaction.TransactionExecutionException;
import exception.transaction.TransactionReadException;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;
import site.ISite;
import site.ITransactional;

/**
 * {@link RVSITransaction} is a kind of transactions with rvsi semantics
 * (i.e., allowed to specify {@link AbstractRVSISpecification}).
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 */
public class RVSITransaction implements ITransaction {

	private final static Logger LOGGER = LoggerFactory.getLogger(RVSITransaction.class);
	
	private final AbstractClientContext context;
	
	private Timestamp sts = Timestamp.TIMESTAMP_INIT;	// start-timestamp
//	private Timestamp cts = Timestamp.TIMESTAMP_INIT_ZERO;	// commit-timestamp

	private final BufferedUpdates buffered_updates = new BufferedUpdates();	
	private final QueryResults query_results = new QueryResults();
	
	private final RVSISpecificationManager rvsi_manager = new RVSISpecificationManager();

	public RVSITransaction(AbstractClientContext context) {
		this.context = context;
	}
	
	/**
	 * To begin a transaction, the client contacts <i>the</i> master to 
	 * acquire a globally unique start timestamp.
	 * 
	 * FIXME Who is responsible for handling the exceptions such as
	 * 	{@link NoRouteToHostException}, {@link ConnectIOException}, and {@link RemoteException}?
	 */
	@Override
	public boolean begin() {
		ISite master = ((ClientContextSingleMaster) context).getMaster();
		
		try {
			this.sts = ((ITransactional) master).start();
			LOGGER.info("The transaction (ID TBD) has successfully obtained a start-timestamp ({}).", sts);
			return true;
		} catch (ConnectIOException cioe) {
			LOGGER.error("An IOException occurs while making a connection to the remote host for a remote method call. \\n {}", cioe.getMessage());
		} catch (RemoteException re) {
			LOGGER.error("An error occurs while contacting the remote master {}. \n {}", master, re.getMessage());
		} catch (TransactionExecutionException te) {
			LOGGER.error(te.getMessage() + "\n" + te.getCause());
		}
		
		return false;
	}

	@Override
	public ITimestampedCell read(Row r, Column c) throws TransactionReadException {
		ISite site = context.getReadSite(new CompoundKey(r, c));
		
		ITimestampedCell ts_cell = TimestampedCell.TIMESTAMPED_CELL_INIT;
		try { 
			ts_cell = site.read(r, c);
			this.query_results.put(new CompoundKey(r, c), ts_cell);
			LOGGER.info("Transaction [{}] read {} from [{}+{}] at site {}", this, ts_cell, r, c, site);
		} catch (RemoteException re) {
			throw new TransactionReadException(String.format("The transaction [%s] failed to read [%s+%s] at site [%s].", this, r, c, site), re.getCause());
		}

		return ts_cell;
	}

	@Override
	public boolean write(Row r, Column c, Cell data) {
		this.buffered_updates.intoBuffer(r, c, data);
		return true;
	}

	/**
	 * Issued by a client to try to commit this transaction.
	 * @return
	 * 	{@code true} if transaction has been committed; {@code false}, otherwise.
	 * @throws TransactionEndException 
	 * 
	 * @implNote
	 * 	This implementation leaves the issues of how to handle with aborted transactions
	 *  to the application.
	 */
	@Override
	public boolean end() throws TransactionEndException {
		VersionConstraintManager vc_manager = this.generateVCManager();
		ToCommitTransaction tx = new ToCommitTransaction(this.sts, this.buffered_updates);
		
		try {
			ISite master = ((ClientContextSingleMaster) context).getMaster();
			return ((ITransactional) master).commit(tx, vc_manager);	
		} catch (RemoteException | TransactionExecutionException re_tee) {
			throw new TransactionEndException(String.format("Transaction [%s] failed to commit.", this), re_tee.getCause());
		}
	}

	/**
	 * Collect {@link AbstractRVSISpecification} whose type could be {@link BVSpecification}, 
	 * {@link FVSpecification}, or {@link SVSpecification}.
	 * 
	 * @param rvsi_spec 
	 * 	an {@link AbstractRVSISpecification}
	 */
	public void collectRVSISpecification(AbstractRVSISpecification rvsi_spec) {
		this.rvsi_manager.collectRVSISpecification(rvsi_spec);
	}
	
	public VersionConstraintManager generateVCManager() {
		return this.rvsi_manager.generateVCManager(this);
	}
	
	public Timestamp getSts() {
		return this.sts;
	}
	
	public QueryResults getQueryResults() {
		return this.query_results;
	}
}
