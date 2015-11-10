package kvs.table;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Objects;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * A {@link TimestampedCell} is a {@link Cell} associated with a {@link Timestamp}.
 */
public class TimestampedCell implements ITimestampedCell
{
//	private Pair<Timestamp, Cell> ts_cell = new ImmutablePair<>(Timestamp.TIMESTAMP_INIT, Cell.CELL_INIT);
	
	private Timestamp ts = Timestamp.TIMESTAMP_INIT;
	private Cell cell = Cell.CELL_INIT;
	
	public TimestampedCell() {}
	
	public TimestampedCell(Timestamp ts, Cell c)
	{
		this.ts = ts;
		this.cell = c;
	}

	@Override
	public Timestamp getTS()
	{
		return this.ts;
	}
	
	@Override
	public Cell getCell()
	{
		return this.cell;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.ts, this.cell);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == null)
			return false;
		if(! (o instanceof TimestampedCell))
			return false;
		
		TimestampedCell that = (TimestampedCell) o;
		return Objects.equal(this.ts, that.ts) && Objects.equal(this.cell, that.cell);
	}
}
