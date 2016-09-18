package client.clientlibrary.transaction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
import timing.ITimestampOracle;
import twopc.TwoPCResult;

import static conf.SiteConfig.simulateInterDCComm;

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
	
	@NotNull
    private Timestamp sts = Timestamp.TIMESTAMP_INIT;	// start-timestamp
//	private Timestamp cts = Timestamp.TIMESTAMP_INIT_ZERO;	// commit-timestamp

	private final BufferedUpdates bufferedUpdates = new BufferedUpdates();
	private final QueryResults queryResults = new QueryResults();
	
	private RVSISpecificationManager rvsiSpecManager = new RVSISpecificationManager();

	public RVSITransaction(AbstractClientContext ctx) { this.cctx = ctx; }
	
	/**
	 * To begin a transaction, the client contacts the {@link timing.ITimestampOracle}
	 * to acquire a globally unique start timestamp.
     *
     * @throws TransactionBeginException thrown if errors occur when acquire a timestamp
	 * 
	 * FIXME Who is responsible for handling the exceptions such as
	 * 	{@link NoRouteToHostException}, {@link ConnectIOException}, and {@link RemoteException}?
	 */
	@Override
	public boolean begin() throws TransactionBeginException {
        simulateInterDCComm();

        ITimestampOracle tsOracle = cctx.getTsOracle();
        LOGGER.debug("The tsOracle for generating sts is [{}].", tsOracle);

		try {
            sts = new Timestamp(tsOracle.get());
			LOGGER.debug("The transaction has successfully obtained a start-timestamp ({}).", sts);
			return true;
		} catch (RemoteException re) {
            throw new TransactionBeginException(String.format("Transaction [%s] failed to begin.", this),
                    re.getCause());
		}
	}

	@Nullable
    @Override
	public ITimestampedCell read(Row r, Column c) throws TransactionReadException {
	    // first look up the last update on (r + c) in the same transaction
	    ITimestampedCell tsCell = bufferedUpdates.lookup(r, c);
	    if (tsCell != null)
	        return tsCell;  // FIXME: no need to put it into the queryResults?

        // then contact the remote site
		ISite site = cctx.getReadSite(new CompoundKey(r, c));
		try {
            simulateInterDCComm();

			tsCell = site.get(r, c);
			queryResults.put(new CompoundKey(r, c), tsCell);
			LOGGER.debug("Transaction [sts: {}] read {} from [{}+{}] at site [{}].",
                    getSts(), tsCell, r, c, site);
		} catch (RemoteException re) {
			throw new TransactionReadException(String.format("The transaction [%s] failed to read [%s+%s] at site [%s].",
                    this, r, c, site), re.getCause());
		}

		return tsCell;
	}

    /**
     * Write data locally.
     * @param r {@link Row}
     * @param c {@link Column}
     * @param data {@link Cell}
     */
	@Override
	public void write(Row r, Column c, Cell data) {
        LOGGER.debug("Transaction [{}] write [{}] to [{}+{}].", this, data, r, c);
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
	public TwoPCResult end() throws TransactionEndException {
		VersionConstraintManager vcm = generateVCManager();
		ToCommitTransaction tx = new ToCommitTransaction(sts, bufferedUpdates);
		
		try {
            simulateInterDCComm();

            TwoPCResult twoPCResult = cctx.getCoord(tx, vcm).execute2PC(tx, vcm);

            boolean isCommitted = twoPCResult.isCommitted();
            LOGGER.debug("Tx [sts: {}] is committed: [{}].", tx.getSts(), isCommitted);
            return twoPCResult;
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

    public void setRvsiSpecManager(RVSISpecificationManager rvsiSpecManager) { this.rvsiSpecManager = rvsiSpecManager; }

    /**
	 * Collect {@link AbstractRVSISpecification} whose type could be {@link BVSpecification}, 
	 * {@link FVSpecification}, or {@link SVSpecification}.
	 * 
	 * @param rvsiSpec
	 * 	an {@link AbstractRVSISpecification}
	 */
	public void collectRVSISpecification(AbstractRVSISpecification rvsiSpec) {
		rvsiSpecManager.collectRVSISpecification(rvsiSpec);
	}
	
	@NotNull
    private VersionConstraintManager generateVCManager() { return rvsiSpecManager.generateVCManager(this); }
	@NotNull
    public Timestamp getSts() { return sts; }
	@NotNull
    public QueryResults getQueryResults() { return queryResults; }

}
