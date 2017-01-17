package kvs.table;

import static org.junit.Assert.*;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 */
public class SingleTimestampedCellStoreTest
{
	@NotNull
    private ITimestampedCellStore store = new SingleTimestampedCellStore();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	}

	/**
	 * Test method for {@link kvs.table.SingleTimestampedCellStore#get()}.
	 */
	@Test
	public void testGet()
	{
		// read 
		ITimestampedCell init_ts_cell = this.store.get();
		assertEquals("This read should obtain the initial value.", new TimestampedCell(), init_ts_cell);
		
		// construct a new {@link ITimestampedCell}
		Timestamp ts = new Timestamp(1L);
		Cell c = new Cell("1L");
		ITimestampedCell ts_cell = new TimestampedCell(ts, c);
		
		// update
		this.store.put(ts_cell);
		
		// the value read before should not be changed
		assertEquals("The value read before should not be changed.", new TimestampedCell(), init_ts_cell);
		
		// read again
		ITimestampedCell second_ts_cell = this.store.get();
		assertEquals("This read should obtain the updated value.", ts_cell, second_ts_cell);
	}

}
