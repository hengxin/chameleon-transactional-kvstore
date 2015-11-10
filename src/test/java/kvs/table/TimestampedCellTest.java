package kvs.table;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TimestampedCellTest
{

	ITimestampedCell ts_cell = new TimestampedCell(new Timestamp(1L), new Cell("TS_Cell"));

	ITimestampedCell ts_cell_smaller = new TimestampedCell();
	ITimestampedCell ts_cell_equal = new TimestampedCell(new Timestamp(1L), new Cell("TS_Cell_Equal"));
	ITimestampedCell ts_cell_larger = new TimestampedCell(new Timestamp(2L), Cell.CELL_INIT);

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testCompareTo()
	{
		assertTrue("The first TimestampedCell should be smaller than the second one.",
				ts_cell_smaller.compareTo(ts_cell) < 0);
		assertTrue("These two TimestampedCells should be equal.", ts_cell_equal.compareTo(ts_cell) == 0);
		assertTrue("The first TimestampedCell should be larger than the second one.", ts_cell_larger.compareTo(ts_cell) > 0);
	}

	@Test
	public void testEquals()
	{
		assertTrue("These two TimestampedCells should be equal.", ts_cell_equal.equals(ts_cell));
	}

}
