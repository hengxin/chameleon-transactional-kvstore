package master.twopc;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import twopc.participant.I2PCParticipant;

/**
 * @author hengxin
 * @date 16-6-7
 */
public class MasterIn2PCTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(MasterIn2PCTest.class);

    @BeforeClass
    public static void setUp() throws Exception {
        Stream.of(
                "twopc/membership-2pc-master-5000.properties",
                "twopc/membership-2pc-master-6000.properties",
                "twopc/membership-2pc-master-8000.properties")
                .forEach(MasterIn2PCTest::launch);

        TimeUnit.MINUTES.sleep(10);
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

    private static void launch(String file) {
        I2PCParticipant master = new MasterIn2PC();
        LOGGER.info("MasterIn2PC [{}] has been successfully launched.", master);
    }

}