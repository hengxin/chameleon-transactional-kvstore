package client.clientlibrary.partitioning;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import client.clientlibrary.transaction.BufferedUpdates;
import kvs.compound.CompoundKey;
import kvs.compound.KVItem;
import kvs.compound.TimestampedCell;

public class ConsistentHashingDynamicPartitionerTest {

	IPartitioner partitioner = new ConsistentHashingDynamicPartitioner();
	
	int buckets = 5;
	CompoundKey ck1 = new CompoundKey("R1", "C1");
	CompoundKey ck2 = new CompoundKey("R2", "C2");
	CompoundKey ck3 = new CompoundKey("R3", "C3");
	
	KVItem item1 = new KVItem(ck1, TimestampedCell.TIMESTAMPED_CELL_INIT);
	KVItem item2 = new KVItem(ck2, TimestampedCell.TIMESTAMPED_CELL_INIT);
	KVItem item3 = new KVItem(ck3, TimestampedCell.TIMESTAMPED_CELL_INIT);

	BufferedUpdates updates = new BufferedUpdates();
	
	@Before
	public void setUp() throws Exception {
		updates.intoBuffer(item1);
		updates.intoBuffer(item2);
		updates.intoBuffer(item3);
	}

	@Test
	public void testLocateSiteIndicesFor() {
		int ck1_hash = partitioner.locateSiteIndexFor(ck1, buckets);
		int ck2_hash = partitioner.locateSiteIndexFor(ck2, buckets);
		int ck3_hash = partitioner.locateSiteIndexFor(ck3, buckets);
		
		Map<Integer, List<KVItem>> actual_partition_map = partitioner.locateSiteIndicesFor(updates, buckets);
		
		Map<KVItem, Integer> expected_hashing_map = ImmutableMap.of(item1, ck1_hash, item2, ck2_hash, item3, ck3_hash);
		Map<Integer, List<KVItem>> expected_partition_map = expected_hashing_map.keySet().stream().collect(Collectors.groupingBy(expected_hashing_map::get));

		assertEquals("", expected_partition_map, actual_partition_map);
	}

}
