package twopc.coordinator;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import client.context.AbstractClientContext;
import exception.transaction.TransactionEndException;
import exception.transaction.TransactionExecutionException;
import kvs.component.Timestamp;
import membership.site.Member;
import rmi.RMIUtil;
import timing.ITimestampOracle;
import twopc.PreparedResult;
import twopc.TransactionCommitResult;
import twopc.coordinator.phaser.CommitPhaser;
import twopc.participant.I2PCParticipant;
import utils.PropertiesUtil;

import static benchmarking.workload.network.NetworkDelayGenerator.simulateInterDCComm;
import static java.util.stream.Collectors.toList;

/**
 * Coordinator of 2PC protocol for RVSI transactions.
 * <p><del> FIXME The coordinator executes an optimized 2PC protocol
 * with "early commit notification".</del>
 * @implNote
 * 	This coordinator implementation is based on class {@link Phaser} introduced since Java 7.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public class RVSI2PCPhaserCoordinator extends Abstract2PCCoordinator {
    private static final long serialVersionUID = -4499972927601545388L;

	private transient static final Logger LOGGER = LoggerFactory.getLogger(RVSI2PCPhaserCoordinator.class);
	private transient static final ExecutorService exec = Executors.newCachedThreadPool();

    Phaser phaser;  // TODO put it in {@link Abstract2PCCoordinator}
    private ITimestampOracle tsOracle;

    /**
	 * @param ctx	client context 
	 */
	public RVSI2PCPhaserCoordinator(@NotNull final AbstractClientContext ctx, String toProperties)  {
		super(ctx);
        phaser = new CommitPhaser(this);   // TODO Is it safe to pass {@code this} reference?

        Properties toProp = null;
        try {
            toProp = PropertiesUtil.load(toProperties);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Set<String> toStrs = toProp.stringPropertyNames();
        String toStr = toStrs.toArray(new String[toStrs.size()])[0];
        Member toMember = Member.parseMember(toProp.getProperty(toStr)).get();
        tsOracle = (ITimestampOracle) RMIUtil.lookup(toMember);
	}

	@Override
	public TransactionCommitResult execute2PC(final ToCommitTransaction tx, final VersionConstraintManager vcm)
            throws RemoteException, TransactionExecutionException {
		final Map<Integer, ToCommitTransaction> siteTxMap = cctx.partition(tx);
        final Map<Integer, VersionConstraintManager> siteVcmMap = cctx.partition(vcm);

        Set<Integer> sites = new HashSet<>();
        sites.addAll(siteTxMap.keySet());
        sites.addAll(siteVcmMap.keySet());

		List<Callable<Boolean>> tasks = sites.stream() // FIXME: sites or siteTxMap.keySet()?
				.map(masterId -> new CommitPhaserTask(this, (I2PCParticipant) cctx.getMaster(masterId),
                        siteTxMap.get(masterId), siteVcmMap.get(masterId)))
                .collect(toList());
		
		try {
			exec.invokeAll(tasks);	// blocking here
		} catch (InterruptedException ie) {
		    String msg = "2PC protocol has been interrupted unexpectedly.";
			LOGGER.error(msg, ie);
            throw new TransactionExecutionException(msg, ie);
		}

		return new TransactionCommitResult(preparedResult, isCommitted);
	}

    /**
     * Check the decisions of all participants during the Phase#PREPARE phase,
     * and determine whether to commit or abort the transaction:
     * if all #prepared_decesions are true, then commit; otherwise, abort.
     */
    @Override
    public boolean onPreparePhaseFinished() throws TransactionEndException {
        toCommitDecision = preparedDecisions.values().stream().allMatch(decision -> decision);

        if (toCommitDecision)
            try {
                simulateInterDCComm();

                cts = new Timestamp(tsOracle.get());
            } catch (RemoteException re) {
                toCommitDecision = false;
                throw new TransactionEndException(String.format("Transaction [%s] failed to begin.", this),
                        re.getCause());
            }

        // more details about prepared results
        preparedResult = preparedResults.values().stream()
                .reduce(PreparedResult.IDENTITY, PreparedResult::accumulate);

        return toCommitDecision;
    }

    /**
     * Check the decisions of all participants during the Phase#COMMIT phase,
     * and compute the final committed/abort state of the transaction:
     * the transaction is committed if and only if
     * (1) #to_committed_decision is true </i>and</i>
     * (2) #comitted_decisions of all participants are true.
     */
    @Override
    public boolean onCommitPhaseFinished() {
        isCommitted = toCommitDecision
                && committedDecisions.values().stream().allMatch(decision -> decision);
        return isCommitted;
    }

    private void readObject(@NotNull ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        phaser = new CommitPhaser(this);
    }

}