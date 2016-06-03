package twopc.coordinator;

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
import twopc.participant.I2PCParticipant;

import static java.util.stream.Collectors.toList;

/**
 * @author hengxin
 * @date 16-6-2
 */
public class RVSI2PCPhaserCoordinatorStub extends RVSI2PCPhaserCoordinator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RVSI2PCPhaserCoordinatorStub.class);
    private final ExecutorService exec = Executors.newCachedThreadPool();

    /**
     * @param ctx client context
     * @param vcm RVSI-specific version constraint manager
     */
    public RVSI2PCPhaserCoordinatorStub(AbstractClientContext ctx, VersionConstraintManager vcm) {
        super(ctx, vcm);
    }

    @Override
    public boolean execute2PC(final ToCommitTransaction tx) {
        List<Callable<Boolean>> task_list = Stream.of() // ISite
                .map(site -> new CommitPhaserTask(this, (I2PCParticipant) site, null, null))
                .collect(toList());

        try {
            exec.invokeAll(task_list);	// blocking here
        } catch (InterruptedException ie) {
            LOGGER.error("2PC protocol has been interrupted unexpectedly.", ie);	// FIXME fault-handling???
        }

        return true;
    }
}
