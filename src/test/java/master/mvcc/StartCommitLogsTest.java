package master.mvcc;

import org.junit.Before;
import org.junit.Test;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;

import static org.junit.Assert.assertTrue;

/**
 * @author hengxin
 * @date Created on 11-23-2015
 */
public final class StartCommitLogsTest {
    private StartCommitLogs logs = new StartCommitLogs();
	
	// keys
    private CompoundKey ck_rx_cx = new CompoundKey("Rx", "Cx");
    private CompoundKey ck_rx_cy = new CompoundKey("Rx", "Cy");
    private CompoundKey ck_rx_cz = new CompoundKey("Rx", "Cz");
    private CompoundKey ck_ry_cy = new CompoundKey("Ry", "Cy");
	
	// start-timestamps and commit-timestamps
    private Timestamp sts_1 = new Timestamp(1);
    private Timestamp sts_4 = new Timestamp(4);
    private Timestamp sts_6 = new Timestamp(6);
    private Timestamp sts_12 = new Timestamp(12);

    private Timestamp cts_10 = new Timestamp(10);
    private Timestamp cts_15 = new Timestamp(15);
    private Timestamp cts_16 = new Timestamp(16);
    private Timestamp cts_20 = new Timestamp(20);
	
	// buffered updates
    private BufferedUpdates updates_1_10 = new BufferedUpdates();
    private BufferedUpdates updates_4_16 = new BufferedUpdates();
    private BufferedUpdates updates_6_20 = new BufferedUpdates();
    private BufferedUpdates updates_12_15 = new BufferedUpdates();

    private Timestamp sts_7_to_commit = new Timestamp(7);
    private BufferedUpdates updates_to_commit_wc = new BufferedUpdates();
    private BufferedUpdates updates_to_commit_wcf = new BufferedUpdates();

	@Before
	public void setUp() throws Exception {
		this.updates_1_10.intoBuffer(ck_rx_cx, Cell.CELL_INIT);
		this.updates_4_16.intoBuffer(ck_rx_cy, Cell.CELL_INIT);
		this.updates_6_20.intoBuffer(ck_rx_cx, Cell.CELL_INIT);
		this.updates_12_15.intoBuffer(ck_ry_cy, Cell.CELL_INIT);
		this.updates_to_commit_wc.intoBuffer(ck_rx_cx, Cell.CELL_INIT);
		this.updates_to_commit_wcf.intoBuffer(ck_rx_cz, Cell.CELL_INIT);
		
		/*
		  commit logs: (1, 10, RxCx), (4, 16, RxCy), (6, 20, RxCx), (12, 15, RyCy)
		 */
		this.logs.addStartCommitLog(sts_1, cts_10, this.updates_1_10);
		this.logs.addStartCommitLog(sts_4, cts_16, this.updates_4_16);
		this.logs.addStartCommitLog(sts_6, cts_20, this.updates_6_20);
		this.logs.addStartCommitLog(sts_12, cts_15, this.updates_12_15);
	}

	@Test
	public void testWCF() {
		ToCommitTransaction tx_to_commit_wc = new ToCommitTransaction(sts_7_to_commit, updates_to_commit_wc);
		assertTrue("Write-Conflict.", ! this.logs.wcf(tx_to_commit_wc));
		
		ToCommitTransaction tx_to_commit_wcf = new ToCommitTransaction(sts_7_to_commit, updates_to_commit_wcf);
		assertTrue("Write-Conflict-Free.", this.logs.wcf(tx_to_commit_wcf));
	}

}
