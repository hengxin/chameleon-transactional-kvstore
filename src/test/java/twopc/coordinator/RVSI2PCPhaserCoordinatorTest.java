package twopc.coordinator;

import org.intellij.lang.annotations.Language;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.context.ClientContextSingleMaster;

/**
 * @author hengxin
 * @date 16-6-8
 */
public class RVSI2PCPhaserCoordinatorTest {
    @Language("Properties")
    private static final String PARTICIPANTS_MEMBERSHIP_FILE = "twopc/membership-2pc-coordinator.properties";

    RVSI2PCPhaserCoordinator coord;

    @Before
    public void setUp() throws Exception {
        coord = new RVSI2PCPhaserCoordinator(new ClientContextSingleMaster(PARTICIPANTS_MEMBERSHIP_FILE, "", ""));
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void execute2PC() throws Exception {

    }

}