package kvs.table;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import kvs.component.Cell;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

public class MultiTimestampedCellsStoreTest
{
	private ITimestampedCellStore multi_ts_cells_store = new MultiTimestampedCellsStore();
	
	private ITimestampedCell ts_cell_0L = new TimestampedCell();
	private ITimestampedCell ts_cell_2L = new TimestampedCell(new Timestamp(2L), new Cell("TS_CELL_2L"));
	private ITimestampedCell ts_cell_4L = new TimestampedCell(new Timestamp(4L), new Cell("TS_CELL_4L"));

	@Before
	public void setUp() throws Exception
	{
		this.multi_ts_cells_store.update(ts_cell_0L);
		this.multi_ts_cells_store.update(ts_cell_4L);
		this.multi_ts_cells_store.update(ts_cell_2L);
	}

	@Test
	public void testGetTimestamp()
	{
		assertEquals("The latest TimestampedCell with respect to 3L should be TS_CELL_2L", ts_cell_2L, this.multi_ts_cells_store.get(new Timestamp(3L)));
	}

	@Test
	public void testGet()
	{
		assertEquals("The latest TimestampedCell should be TS_CELL_4L", ts_cell_4L, this.multi_ts_cells_store.get());
	}

}
