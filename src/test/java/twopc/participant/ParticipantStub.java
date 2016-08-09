package twopc.participant;

import com.sun.istack.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import context.AbstractContext;
import master.AbstractMaster;
import messages.IMessageProducer;
import twopc.coordinator.RVSI2PCPhaserCoordinatorStub;

/**
 * @author hengxin
 * @date 16-6-2
 */
public class ParticipantStub extends AbstractMaster implements I2PCParticipant {
    private static final Logger LOGGER = LoggerFactory.getLogger(RVSI2PCPhaserCoordinatorStub.class);
    private static final Random rand = new Random();

    /**
     * @param context   context for the master site
     * @param messenger the underlying mechanism of message propagation;
     *                  it can be {@code null} if this master site does not need to propagate messages.
     */
    public ParticipantStub(AbstractContext context, @Nullable IMessageProducer messenger) {
        super(context, messenger);
    }

    @Override
    public boolean prepare(ToCommitTransaction tx, VersionConstraintManager vcm) {
        LOGGER.debug("The participant [{}] is in Thread [{}].", this, Thread.currentThread());

        try {
            long duration = (long) (Math.random() * 10);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        boolean prepared = rand.nextBoolean();
        LOGGER.info("In PREPARE phase, reply with [{}]", prepared);
        return prepared;
    }

    @Override
    public boolean complete() {
        boolean committed = rand.nextBoolean();
        LOGGER.info("In COMMIT phase, reply with [{}]", committed);
        return committed;
    }
}
