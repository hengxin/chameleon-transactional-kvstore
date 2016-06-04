package twopc.coordinator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import network.membership.AbstractStaticMembership;
import network.membership.MasterMembership;
import site.AbstractSite;
import site.ISite;

/**
 * @author hengxin
 * @date 16-6-3
 */
public class RVSI2PCPhaserCoordinatorStubTest {

    private static final String PARTICIPANTS_MEMBERSHIP_FILE =
            "twopc/local/coordinator/membership-coordinator.properties";

    private RVSI2PCPhaserCoordinatorStub coord;

    @Before
    public void setUp() throws Exception {
        AbstractStaticMembership membership = new MasterMembership(PARTICIPANTS_MEMBERSHIP_FILE);
        List<ISite> participants = AbstractSite.locateRMISites(((MasterMembership) membership).getSlaves());
        coord = new RVSI2PCPhaserCoordinatorStub(null, new VersionConstraintManager(Collections.emptyList()),
                participants.stream());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void execute2PC() throws Exception {
        coord.execute2PC(null);
    }

}