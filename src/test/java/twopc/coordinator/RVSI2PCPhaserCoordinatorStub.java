package twopc.coordinator;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import client.context.AbstractClientContext;
import site.ISite;
import twopc.TwoPCResult;
import twopc.participant.I2PCParticipant;

import static java.util.stream.Collectors.toList;

/**
 * @author hengxin
 * @date 16-6-2
 */
public class RVSI2PCPhaserCoordinatorStub extends RVSI2PCPhaserCoordinator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RVSI2PCPhaserCoordinatorStub.class);
    private final ExecutorService exec = Executors.newCachedThreadPool();

    private Stream<ISite> rmiSites;

    /**
     * @param ctx client context
     * @param sites specify the participant sites directly (for test)
     */
    public RVSI2PCPhaserCoordinatorStub(@NotNull AbstractClientContext ctx,
                                        Stream<ISite> sites, String toProperties) {
        super(ctx, toProperties);
        this.rmiSites = sites;
    }

    @Override
    public TwoPCResult execute2PC(final ToCommitTransaction tx, final VersionConstraintManager vcm) {
        List<Callable<Boolean>> task_list = rmiSites
                .map(site -> new CommitPhaserTask(this, (I2PCParticipant) site, null, null))
                .collect(toList());

        try {
            exec.invokeAll(task_list);	// blocking here
        } catch (InterruptedException ie) {
            LOGGER.error("2PC protocol has been interrupted unexpectedly.", ie);	// FIXME fault-handling???
        }

        LOGGER.info("2PC protocol has been finished.");

        return null;
    }
}
