package jms;

import static org.junit.Assert.assertEquals;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Before;
import org.junit.Test;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import messaging.jms.master.JMSPublisher;
import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;
import messaging.AbstractMessage;
import messaging.IMessageProducer;
import slave.RCSlave;

public class RCSlaveAsJMSCommitLogSubscriberTest {
	private final IMessageProducer publisher = new JMSPublisher();
	
	private Timestamp sts; 
	@NotNull
    private CompoundKey ck_rx_cx = new CompoundKey("Rx", "Cx");
	@NotNull
    private CompoundKey ck_ry_cy = new CompoundKey("Ry", "Cy");
	@NotNull
    private Cell cell_rx_cx = new Cell("RxCx");
	@NotNull
    private Cell cell_ry_cy = new Cell("RyCy");
	
	@NotNull
    private BufferedUpdates buffered_updates = new BufferedUpdates();
	@Nullable
    private AbstractMessage commit_log_message = null;
	
	@Nullable
    private RCSlave slave = null;

	@Before
	public void setUp() throws Exception {
		this.slave = new RCSlave(null);	// no {@link IContext} needed for testing JMS
		
		this.sts = new Timestamp(1);
		this.buffered_updates.intoBuffer(this.ck_rx_cx, this.cell_rx_cx);
		this.buffered_updates.intoBuffer(this.ck_ry_cy, this.cell_ry_cy);
		this.commit_log_message = new ToCommitTransaction(this.sts, this.buffered_updates);

		this.publisher.send(this.commit_log_message);
	}

	@Test
	public void testOnMessageOfRCSlave() throws InterruptedException {
		// wait a moment for the subscriber to receive the {@link ToCommitTransaction} log and update the {@link #table}
		Thread.sleep(2000);

		ITimestampedCell ts_cell_rx_cx = this.slave.get(this.ck_rx_cx.getRow(), this.ck_rx_cx.getCol());
		assertEquals("Should lookup the updated cell: RxCx.", this.cell_rx_cx, ts_cell_rx_cx.getCell());
		
		ITimestampedCell ts_cell_ry_cy = this.slave.get(this.ck_ry_cy.getRow(), this.ck_ry_cy.getCol());
		assertEquals("Should lookup the updated cell: RyCy.", this.cell_ry_cy, ts_cell_ry_cy.getCell());
		
		ITimestampedCell ts_cell_no_such_data = this.slave.get(this.ck_rx_cx.getRow(), this.ck_ry_cy.getCol());
		assertEquals("Should lookup the initial cell.", TimestampedCell.TIMESTAMPED_CELL_INIT, ts_cell_no_such_data);
	}
}
