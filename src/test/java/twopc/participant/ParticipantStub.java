package twopc.participant;

import com.sun.istack.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import context.IContext;
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
    public ParticipantStub(IContext context, @Nullable IMessageProducer messenger) {
        super(context, messenger);
    }

    @Override
    public boolean prepare(ToCommitTransaction tx, VersionConstraintManager vcm) {
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
