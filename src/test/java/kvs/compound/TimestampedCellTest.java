package kvs.compound;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import kvs.component.Cell;
import kvs.component.Ordinal;
import kvs.component.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimestampedCellTest {

	@NotNull ITimestampedCell ts_cell = new TimestampedCell(new Timestamp(1L), Ordinal.ORDINAL_INIT(), new Cell("TS_Cell"));

	@NotNull ITimestampedCell ts_cell_smaller = new TimestampedCell();
	@NotNull ITimestampedCell ts_cell_equal = new TimestampedCell(new Timestamp(1L), new Ordinal(2L), new Cell("TS_Cell_Equal"));
	@NotNull ITimestampedCell ts_cell_larger = new TimestampedCell(new Timestamp(2L), new Ordinal(0L), Cell.CELL_INIT);

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCompareTo() {
		assertTrue("The first TimestampedCell should be smaller than the second one.",
				ts_cell_smaller.compareTo(ts_cell) < 0);
		assertTrue("These two TimestampedCells should be equal.", ts_cell_equal.compareTo(ts_cell) == 0);
		assertTrue("The first TimestampedCell should be larger than the second one.", ts_cell_larger.compareTo(ts_cell) > 0);
	}

	@Test
	public void testEquals() {
		assertEquals("These two TimestampedCells should be equal.", ts_cell, ts_cell_equal);
	}

}
