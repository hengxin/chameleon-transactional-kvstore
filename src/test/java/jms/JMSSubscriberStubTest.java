package jms;

import static org.junit.Assert.*;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import messaging.jms.AbstractJMSParticipant;
import messaging.jms.master.JMSPublisher;
import messaging.jms.slave.JMSSubscriber;
import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;
import kvs.table.AbstractTable;
import kvs.table.SlaveTable;
import messaging.AbstractMessage;
import messaging.IMessageProducer;

/**
 * Mock {@link JMSSubscriber} using {@link JMSSubscriberStub} 
 * and test it accept() method.
 * 
 * @author hengxin
 * @date Created on 11-25-2015
 */
public class JMSSubscriberStubTest {

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
	
	@NotNull
    private AbstractTable table = new SlaveTable();
	@Nullable
    @SuppressWarnings("unused")
	private AbstractJMSParticipant subscriber = null;

	@Before
	public void setUp() throws Exception {
		this.subscriber = new JMSSubscriberStub(this.table);
		
		this.sts = new Timestamp(1);
		this.buffered_updates.intoBuffer(this.ck_rx_cx, this.cell_rx_cx);
		this.buffered_updates.intoBuffer(this.ck_ry_cy, this.cell_ry_cy);
		this.commit_log_message = new ToCommitTransaction(this.sts, this.buffered_updates);

		Assert.assertNotNull("The message to be published cannot be null.", this.commit_log_message);

		this.publisher.send(this.commit_log_message);
	}

	@Test
	public void testOnMessage() throws InterruptedException {
		// wait a moment for the subscriber to receive the {@link ToCommitTransaction} log and update the {@link #table}
		Thread.sleep(2000);

		ITimestampedCell ts_cell_rx_cx = this.table.getTimestampedCell(this.ck_rx_cx.getRow(), this.ck_rx_cx.getCol());
		assertEquals("Should lookup the updated cell: RxCx.", this.cell_rx_cx, ts_cell_rx_cx.getCell());
		
		ITimestampedCell ts_cell_ry_cy = this.table.getTimestampedCell(this.ck_ry_cy.getRow(), this.ck_ry_cy.getCol());
		assertEquals("Should lookup the updated cell: RyCy.", this.cell_ry_cy, ts_cell_ry_cy.getCell());
		
		ITimestampedCell ts_cell_no_such_data = this.table.getTimestampedCell(this.ck_rx_cx.getRow(), this.ck_ry_cy.getCol());
		assertEquals("Should lookup the initial cell.", TimestampedCell.TIMESTAMPED_CELL_INIT, ts_cell_no_such_data);
	}
	
	/**
	 * @author hengxin
	 * @date 11-25-2015
	 * 
	 * Mock {@link JMSSubscriber}, then we can test its accept() behavior.
	 */
	private final class JMSSubscriberStub extends JMSSubscriber {

		@Nullable
        private AbstractTable table = null;
		
		public JMSSubscriberStub(AbstractTable table) {
			this.table = table;
		}
		
		@Override
		public void onMessage(Message msg) {
			ObjectMessage obj_msg = (ObjectMessage) msg;
			try {
				ToCommitTransaction commit_log_msg = (ToCommitTransaction) obj_msg.getObject();
				this.table.apply(commit_log_msg.getSts(), commit_log_msg.getBufferedUpdates());
			} catch (JMSException jmse) {
				System.err.format("Fail to receive the message: %s.", jmse.getMessage());
				jmse.printStackTrace();
				System.exit(1);
			}
		}
	}

}
