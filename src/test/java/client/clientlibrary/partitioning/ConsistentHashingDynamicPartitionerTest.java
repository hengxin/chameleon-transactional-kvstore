package client.clientlibrary.partitioning;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.component.Timestamp;
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
	ToCommitTransaction tx;
	
	BufferedUpdates updates1 = new BufferedUpdates();
	BufferedUpdates updates2 = new BufferedUpdates();
	BufferedUpdates updates3 = new BufferedUpdates();
	
	ToCommitTransaction tx1, tx2, tx3;
	
	@Before
	public void setUp() throws Exception {
		updates.intoBuffer(item1);
		updates.intoBuffer(item2);
		updates.intoBuffer(item3);
		
		tx = new ToCommitTransaction(Timestamp.TIMESTAMP_INIT, updates);
		
		updates1.intoBuffer(item1);
		updates2.intoBuffer(item2);
		updates3.intoBuffer(item3);
		
		tx1 = new ToCommitTransaction(Timestamp.TIMESTAMP_INIT, updates1);
		tx2 = new ToCommitTransaction(Timestamp.TIMESTAMP_INIT, updates2);
		tx3 = new ToCommitTransaction(Timestamp.TIMESTAMP_INIT, updates3);
	}

	@Test
	public void testLocateSiteIndicesFor() {
		int ck1_hash = partitioner.locateSiteIndexFor(ck1, buckets);
		int ck2_hash = partitioner.locateSiteIndexFor(ck2, buckets);
		int ck3_hash = partitioner.locateSiteIndexFor(ck3, buckets);
		
		Map<Integer, ToCommitTransaction> actual_partition_map = partitioner.partition(tx, buckets);
		
		Map<ToCommitTransaction, Integer> expected_hashing_map = ImmutableMap.of(tx1, ck1_hash, tx2, ck2_hash, tx3, ck3_hash);
		Map<Integer, ToCommitTransaction> expected_partition_map = expected_hashing_map.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, ToCommitTransaction::merge));

		assertEquals("", expected_partition_map, actual_partition_map);
	}

}
