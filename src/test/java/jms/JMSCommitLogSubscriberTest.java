package jms;

import static org.junit.Assert.fail;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.junit.Before;
import org.junit.Test;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import jms.master.JMSCommitLogPublisher;
import jms.slave.JMSCommitLogSubscriber;
import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;
import kvs.table.SlaveTable;
import messages.AbstractMessage;

public class JMSCommitLogSubscriberTest
{
	private final AbstractJMSParticipant publisher = new JMSCommitLogPublisher();
	
	private Timestamp sts; 
	private CompoundKey ck_rx_cx = new CompoundKey("Rx", "Cx");
	private CompoundKey ck_ry_cy = new CompoundKey("Ry", "Cy");
	private Cell cell_rx_cx = new Cell("RxCx");
	private Cell cell_ry_cy = new Cell("RyCy");
	
	private BufferedUpdates buffered_updates = new BufferedUpdates();

	private AbstractMessage commit_log_message = null;
	
	private AbstractTable table = new SlaveTable();
	private AbstractJMSParticipant subscriber = null;

	@Before
	public void setUp() throws Exception
	{
		this.subscriber = new JMSCommitLogSubscriberStub(this.table);
		
		this.sts = new Timestamp(1);
		this.buffered_updates.intoBuffer(this.ck_rx_cx, this.cell_rx_cx);
		this.buffered_updates.intoBuffer(this.ck_ry_cy, this.cell_ry_cy);
		this.commit_log_message = new ToCommitTransaction(this.sts, this.buffered_updates);

		((JMSCommitLogPublisher) this.publisher).publish(this.commit_log_message);
	}

	@Test
	public void testParticipate()
	{
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testOnMessage()
	{
		ITimestampedCell ts_cell = this.table.getTimestampedCell(this.ck_rx_cx.getRow(), this.ck_ry_cy.getCol());
	}
	
	/**
	 * @author hengxin
	 * @date 11-25-2015
	 * 
	 * Mock {@link JMSCommitLogSubscriber}, then we can test its onMessage() behavior.
	 */
	private final class JMSCommitLogSubscriberStub extends JMSCommitLogSubscriber
	{
		private AbstractTable table = null;
		
		public JMSCommitLogSubscriberStub(AbstractTable table)
		{
			this.table = table;
		}
		
		@Override
		public void onMessage(Message msg)
		{
			ObjectMessage obj_msg = (ObjectMessage) msg;
			try
			{
				ToCommitTransaction commit_log_msg = (ToCommitTransaction) obj_msg.getObject();
				this.table.apply(commit_log_msg.getSts(), commit_log_msg.getBuffered_Updates());
			} catch (JMSException jmse)
			{
				System.out.format("Fail to receive the message: %s.", jmse.getMessage());
				jmse.printStackTrace();
				System.exit(1);
			}
		}
	}

}
