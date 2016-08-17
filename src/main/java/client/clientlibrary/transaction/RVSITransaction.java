package client.clientlibrary.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.NoRouteToHostException;
import java.rmi.ConnectIOException;
import java.rmi.RemoteException;

import client.clientlibrary.rvsi.rvsimanager.RVSISpecificationManager;
import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.rvsi.rvsispec.FVSpecification;
import client.clientlibrary.rvsi.rvsispec.SVSpecification;
import client.context.AbstractClientContext;
import exception.transaction.TransactionBeginException;
import exception.transaction.TransactionEndException;
import exception.transaction.TransactionExecutionException;
import exception.transaction.TransactionReadException;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import site.ISite;
import twopc.timing.ITimestampOracle;

/**
 * {@link RVSITransaction} is a kind of transactions with rvsi semantics
 * (i.e., allowed to specify {@link AbstractRVSISpecification}).
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 */
public class RVSITransaction implements ITransaction {

	private final static Logger LOGGER = LoggerFactory.getLogger(RVSITransaction.class);
	
	private final AbstractClientContext cctx;
	
	private Timestamp sts = Timestamp.TIMESTAMP_INIT;	// start-timestamp
//	private Timestamp cts = Timestamp.TIMESTAMP_INIT_ZERO;	// commit-timestamp

	private final BufferedUpdates bufferedUpdates = new BufferedUpdates();
	private final QueryResults query_results = new QueryResults();
	
	private final RVSISpecificationManager rvsi_manager = new RVSISpecificationManager();

	public RVSITransaction(AbstractClientContext ctx) {
		this.cctx = ctx;
	}
	
	/**
	 * To begin a transaction, the client contacts the {@link twopc.timing.ITimestampOracle}
	 * to acquire a globally unique start timestamp.
     *
     * @throws TransactionBeginException thrown if errors occur when acquire a timestamp
	 * 
	 * FIXME Who is responsible for handling the exceptions such as
	 * 	{@link NoRouteToHostException}, {@link ConnectIOException}, and {@link RemoteException}?
	 */
	@Override
	public boolean begin() throws TransactionBeginException {

        ITimestampOracle tsOracle = cctx.getTsOracle();
		try {
            sts = new Timestamp(tsOracle.get());
			LOGGER.info("The transaction (ID TBD) has successfully obtained a start-timestamp ({}).", sts);
			return true;
		} catch (RemoteException re) {
            throw new TransactionBeginException(String.format("Transaction [%s] failed to begin.", this),
                    re.getCause());
		}
	}

	@Override
	public ITimestampedCell read(Row r, Column c) throws TransactionReadException {
		ISite site = cctx.getReadSite(new CompoundKey(r, c));
		
		ITimestampedCell ts_cell;
		try { 
			ts_cell = site.get(r, c);
			this.query_results.put(new CompoundKey(r, c), ts_cell);
			LOGGER.info("Transaction [{}] read {} from [{}+{}] at site {}", this, ts_cell, r, c, site);
		} catch (RemoteException re) {
			throw new TransactionReadException(String.format("The transaction [%s] failed to read [%s+%s] at site [%s].", this, r, c, site), re.getCause());
		}

		return ts_cell;
	}

    /**
     * Write data locally.
     * @param r {@link Row}
     * @param c {@link Column}
     * @param data {@link Cell}
     */
	@Override
	public void write(Row r, Column c, Cell data) {
		bufferedUpdates.intoBuffer(r, c, data);
	}

	/**
	 * Issued by a client to try to commit this transaction.
	 * @return 	{@code true} if transaction has been committed; {@code false}, otherwise.
	 * @throws TransactionEndException 
	 * 
	 * @implNote
	 * 	This implementation leaves the issues of how to handle with aborted transactions
	 *  to the application.
	 */
	@Override
	public boolean end() throws TransactionEndException {
		VersionConstraintManager vcm = generateVCManager();
		ToCommitTransaction tx = new ToCommitTransaction(sts, bufferedUpdates);
		
		try {
            return cctx.getCoord(tx).execute2PC(tx, vcm);
		} catch (RemoteException re) {
			throw new TransactionEndException(
			        String.format("Transaction [%s] failed to commit due to RMI-related issues.", this),
                    re.getCause());
		} catch (TransactionExecutionException tee) {
		    throw new TransactionEndException(
		            String.format("Transaction [%s] failed to commit.", this),
                    tee.getCause());
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
	
	public VersionConstraintManager generateVCManager() { return rvsi_manager.generateVCManager(this); }
	
	public Timestamp getSts() { return sts; }
	
	public QueryResults getQueryResults() { return query_results; }
}
