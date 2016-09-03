package twopc.participant;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import messaging.jms.master.JMSPublisher;
import master.AbstractMaster;
import master.context.MasterContext;

/**
 * @author hengxin
 * @date 16-6-2
 */
public class ParticipantStubTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ParticipantStubTest.class);

    /**
     * Export two masters (as participants in 2PC protocol) in the same JVM.
     * @throws Exception
     */
    @BeforeClass
    public static void setUpParticipants() throws Exception {
        final Stream<String> masterPropFiles = Stream.of(
                "twopc/local/participant/membership-master-5000.properties",
                "twopc/local/participant/membership-master-6000.properties");

        masterPropFiles.forEach(prop -> {
            AbstractMaster participantMaster = new ParticipantStub(new MasterContext(prop), new JMSPublisher());
            LOGGER.info("MasterIn2PC [{}] has been successfully launched.", participantMaster);
        });

        TimeUnit.SECONDS.sleep(100);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void prepare() throws Exception {

    }

    @Test
    public void complete() throws Exception {

    }

}