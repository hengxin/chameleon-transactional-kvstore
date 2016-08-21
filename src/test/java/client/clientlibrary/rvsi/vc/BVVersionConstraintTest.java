package client.clientlibrary.rvsi.vc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.clientlibrary.partitioning.ConsistentHashingDynamicPartitioner;
import client.clientlibrary.partitioning.IPartitioner;
import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;

import static org.junit.Assert.assertEquals;

/**
 * @author hengxin
 * @date 16-6-6
 */
public class BVVersionConstraintTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BVVersionConstraintTest.class);

    private static final int BUCKETS = 10;
    private IPartitioner partitioner = ConsistentHashingDynamicPartitioner.INSTANCE;

    private CompoundKey ckR1C1 = new CompoundKey("R1", "C1");
    private CompoundKey ckR2C2 = new CompoundKey("R2", "C2");

    private Timestamp sts = new Timestamp(5L);

    private VCEntry vceR1C1 = new VCEntry(ckR1C1, new Ordinal(1), sts, 1);
    private VCEntry vceR2C2 = new VCEntry(ckR2C2, new Ordinal(2), sts, 2);

    private List<VCEntry> vcEntries = new ArrayList<>();

    private AbstractVersionConstraint bvVc;

    @Before
    public void setUp() throws Exception {
        vcEntries.add(vceR1C1);
        vcEntries.add(vceR2C2);

        bvVc = new BVVersionConstraint(vcEntries);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void check() throws Exception {

    }

    @Test
    public void partition() throws Exception {
        int siteR1C1 = partitioner.locateSiteIndexFor(ckR1C1, BUCKETS);
        int siteR2C2 = partitioner.locateSiteIndexFor(ckR2C2, BUCKETS);

        Map<Integer, AbstractVersionConstraint> expectedMap = new HashMap<>();
        expectedMap.put(siteR1C1, new BVVersionConstraint(Arrays.asList(vceR1C1)));
        expectedMap.put(siteR2C2, new BVVersionConstraint(Arrays.asList(vceR2C2)));

        assertEquals("Partition version constraint as expected.", expectedMap, bvVc.partition(partitioner, BUCKETS));
    }

}