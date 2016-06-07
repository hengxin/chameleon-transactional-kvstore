package client.clientlibrary.rvsi.rvsimanager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.ConsistentHashingDynamicPartitioner;
import client.clientlibrary.partitioning.IPartitioner;
import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.rvsi.vc.BVVersionConstraint;
import client.clientlibrary.rvsi.vc.FVVersionConstraint;
import client.clientlibrary.rvsi.vc.VCEntry;
import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

/**
 * @author hengxin
 * @date 16-6-6
 */
public class VersionConstraintManagerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(VersionConstraintManagerTest.class);

    private static final int BUCKETS = 10;
    private IPartitioner partitioner = ConsistentHashingDynamicPartitioner.INSTANCE;

    private CompoundKey ckR1C1 = new CompoundKey("R1", "C1");
    private CompoundKey ckR2C2 = new CompoundKey("R2", "C2");

    private Timestamp sts = new Timestamp(5L);

    private VCEntry bvVceR1C1 = new VCEntry(ckR1C1, new Ordinal(1), sts, 1L);
    private VCEntry bvVceR2C2 = new VCEntry(ckR2C2, new Ordinal(2), sts, 2L);
    private VCEntry fvVceR1C1 = new VCEntry(ckR1C1, new Ordinal(11), sts, 1L);
    private VCEntry fvVceR2C2 = new VCEntry(ckR2C2, new Ordinal(22), sts, 2L);

    private List<VCEntry> bvVcEntries = asList(bvVceR1C1, bvVceR2C2);
    private List<VCEntry> fvVcEntries = asList(fvVceR1C1, fvVceR2C2);

    private AbstractVersionConstraint bvVc = new BVVersionConstraint(bvVcEntries);
    private AbstractVersionConstraint fvVc = new FVVersionConstraint(fvVcEntries);

    private VersionConstraintManager vcm = new VersionConstraintManager(asList(bvVc, fvVc));

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }
    @Test
    public void check() throws Exception {

    }

    @Test
    public void partition() throws Exception {
        Map<Integer, VersionConstraintManager> vcmMap = vcm.partition(partitioner, BUCKETS);

        // construct expected result
        int site4R1C1 = partitioner.locateSiteIndexFor(ckR1C1, BUCKETS);
        int site4R2C2 = partitioner.locateSiteIndexFor(ckR2C2, BUCKETS);

        AbstractVersionConstraint r1c1BvVc = new BVVersionConstraint(asList(bvVceR1C1));
        AbstractVersionConstraint r2c2BvVc = new BVVersionConstraint(asList(bvVceR2C2));
        AbstractVersionConstraint r1c1FvVc = new FVVersionConstraint(asList(fvVceR1C1));
        AbstractVersionConstraint r2c2FvVc = new FVVersionConstraint(asList(fvVceR2C2));

        Map<Integer, VersionConstraintManager> expectedVcmMap = new HashMap<>();
        expectedVcmMap.put(site4R1C1, new VersionConstraintManager(asList(r1c1BvVc, r1c1FvVc)));
        expectedVcmMap.put(site4R2C2, new VersionConstraintManager(asList(r2c2BvVc, r2c2FvVc)));

        assertEquals("Partition version constraint manager successfully.", expectedVcmMap, vcmMap);
    }

}