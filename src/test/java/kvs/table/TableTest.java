package kvs.table;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

public class TableTest
{
	private AbstractTable master_table = new MasterTable(); // using {@link MultiTimestampedCellsStore}
	private AbstractTable slave_table = new SlaveTable(); // using {@link SingleTimestampedCellStore}

	ITimestampedCell ts_cell_0L = null;
	ITimestampedCell ts_cell_2L = null;
	ITimestampedCell ts_cell_4L = null;

	@Before
	public void setUp() throws Exception
	{
		Row row = new Row("R1");
		Column col = new Column("C1");

		ts_cell_0L = new TimestampedCell(Timestamp.TIMESTAMP_INIT_ZERO, Cell.CELL_INIT);
		ts_cell_2L = new TimestampedCell(new Timestamp(2L), new Cell("TS_CELL_2L"));
		ts_cell_4L = new TimestampedCell(new Timestamp(4L), new Cell("TS_CELL_4L"));

		// fill the {@value #master_table}
		this.master_table.put(row, col, ts_cell_2L);
		this.master_table.put(row, col, ts_cell_4L);
		this.master_table.put(row, col, ts_cell_0L);

		// fill the {@value #slave_table}
		this.slave_table.put(row, col, ts_cell_2L);
		this.slave_table.put(row, col, ts_cell_4L);
		this.slave_table.put(row, col, ts_cell_0L);
	}

	@Test
	public void testGetTimestampedCellRowColumn()
	{
		ITimestampedCell ts_cell_master = this.master_table.getTimestampedCell(new Row("R1"), new Column("C1"));
		assertEquals("The latest data in the master table should be TS_CELL_4L.", ts_cell_4L, ts_cell_master);
		
		ITimestampedCell ts_cell_slave = this.slave_table.getTimestampedCell(new Row("R1"), new Column("C1"));
		assertEquals("The latest data in the slave table should be TS_CELL_0L", ts_cell_0L, ts_cell_slave);
	}

	@Test
	public void testGetTimestampedCellRowColumnTimestamp()
	{
		ITimestampedCell ts_cell_master = this.master_table.getTimestampedCell(new Row("R1"), new Column("C1"), new Timestamp(3L));
		assertEquals("The last preceding data before TS_CELL_3L in the master table should be TS_CELL_2L.", ts_cell_2L, ts_cell_master);
		
		ITimestampedCell ts_cell_slave = this.slave_table.getTimestampedCell(new Row("R1"), new Column("C1"), new Timestamp(3L));
		assertEquals("It should obtain the latest data TS_CELL_0L in the slave table.", ts_cell_0L, ts_cell_slave);
	}

}
