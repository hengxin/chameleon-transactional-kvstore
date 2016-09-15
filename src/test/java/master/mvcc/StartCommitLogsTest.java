package master.mvcc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;

/**
 * @author hengxin
 * @date Created on 11-23-2015
 */
public class StartCommitLogsTest
{
	@NotNull
    private StartCommitLogs logs = new StartCommitLogs();
	
	// keys
    @NotNull CompoundKey ck_rx_cx = new CompoundKey("Rx", "Cx");
	@NotNull CompoundKey ck_rx_cy = new CompoundKey("Rx", "Cy");
	@NotNull CompoundKey ck_rx_cz = new CompoundKey("Rx", "Cz");
	@NotNull CompoundKey ck_ry_cy = new CompoundKey("Ry", "Cy");
	
	// start-timestamps and commit-timestamps
    @NotNull Timestamp sts_1 = new Timestamp(1);
	@NotNull Timestamp sts_4 = new Timestamp(4);
	@NotNull Timestamp sts_6 = new Timestamp(6);
	@NotNull Timestamp sts_12 = new Timestamp(12);

	@NotNull Timestamp cts_10 = new Timestamp(10);
	@NotNull Timestamp cts_15 = new Timestamp(15);
	@NotNull Timestamp cts_16 = new Timestamp(16);
	@NotNull Timestamp cts_20 = new Timestamp(20);
	
	// buffered updates
    @NotNull BufferedUpdates updates_1_10 = new BufferedUpdates();
	@NotNull BufferedUpdates updates_4_16 = new BufferedUpdates();
	@NotNull BufferedUpdates updates_6_20 = new BufferedUpdates();
	@NotNull BufferedUpdates updates_12_15 = new BufferedUpdates();

	@NotNull Timestamp sts_7_to_commit = new Timestamp(7);
	@NotNull BufferedUpdates updates_to_commit_wc = new BufferedUpdates();
	@NotNull BufferedUpdates updates_to_commit_wcf = new BufferedUpdates();

	@Before
	public void setUp() throws Exception
	{
		this.updates_1_10.intoBuffer(ck_rx_cx, Cell.CELL_INIT);
		this.updates_4_16.intoBuffer(ck_rx_cy, Cell.CELL_INIT);
		this.updates_6_20.intoBuffer(ck_rx_cx, Cell.CELL_INIT);
		this.updates_12_15.intoBuffer(ck_ry_cy, Cell.CELL_INIT);
		this.updates_to_commit_wc.intoBuffer(ck_rx_cx, Cell.CELL_INIT);
		this.updates_to_commit_wcf.intoBuffer(ck_rx_cz, Cell.CELL_INIT);
		
		/**
		 * commit logs: (1, 10, RxCx), (4, 16, RxCy), (6, 20, RxCx), (12, 15, RyCy)
		 */
		this.logs.addStartCommitLog(sts_1, cts_10, this.updates_1_10);
		this.logs.addStartCommitLog(sts_4, cts_16, this.updates_4_16);
		this.logs.addStartCommitLog(sts_6, cts_20, this.updates_6_20);
		this.logs.addStartCommitLog(sts_12, cts_15, this.updates_12_15);
	}

	@Test
	public void testContainersOf()
	{
		Collection<BufferedUpdates> container_updates;
		container_updates = this.logs.containersOf(sts_7_to_commit);
		
		assertEquals("There should be 3 containers for this start-timestamp.", 3, container_updates.size());

		List<BufferedUpdates> expected_container_updates = new ArrayList<>();
		expected_container_updates.add(updates_1_10);
		expected_container_updates.add(updates_4_16);
		expected_container_updates.add(updates_6_20);
		
		assertTrue(container_updates.containsAll(expected_container_updates));
	}

	@Test
	public void testWCF()
	{
		ToCommitTransaction tx_to_commit_wc = new ToCommitTransaction(sts_7_to_commit, updates_to_commit_wc);
		assertTrue("Write-Conflict.", ! this.logs.wcf(tx_to_commit_wc));
		
		ToCommitTransaction tx_to_commit_wcf = new ToCommitTransaction(sts_7_to_commit, updates_to_commit_wcf);
		assertTrue("Write-Conflict-Free.", this.logs.wcf(tx_to_commit_wcf));
	}
}
