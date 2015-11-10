package kvs.table;

import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * A {@link TimestampedCell} is a {@link Cell} associated with a {@link Timestamp}.
 */
public class TimestampedCell implements ITimestampedCell
{
	private Timestamp ts = Timestamp.TIMESTAMP_INIT;
	private Cell cell = Cell.CELL_INIT;
	
	/**
	 * default constructor: with {@value Timestamp#TIMESTAMP_INIT} and {@value Cell#CELL_INIT}
	 */
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
	public int compareTo(ITimestampedCell that)
	{
		return ComparisonChain.start().compare(this.ts, ((TimestampedCell) that).ts).result();
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
		return Objects.equal(this.ts, that.ts); // && Objects.equal(this.cell, that.cell); // uniquely identified by its {@link Timestamp}
	}

}
