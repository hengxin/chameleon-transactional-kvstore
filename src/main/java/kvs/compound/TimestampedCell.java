package kvs.compound;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import kvs.component.Cell;
import kvs.component.Ordinal;
import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * A {@link TimestampedCell} is a {@link Cell} associated with a {@link Timestamp} (beside its {@link Ordinal}).
 * 
 * <p> <b>Note:</b> A {@link TimestampedCell} is uniquely identified by its {@value #ts} ({@link Timestamp}) field.
 * See its {@link #compareTo(ITimestampedCell)}, {@link #hashCode()}, and {@link #equals(Object)}.
 */
public class TimestampedCell implements ITimestampedCell
{
	private Timestamp ts = Timestamp.TIMESTAMP_INIT;
	private Ordinal ord = Ordinal.ORDINAL_INIT;
	private Cell cell = Cell.CELL_INIT;
	
	/**
	 * initial value: {@value Timestamp#TIMESTAMP_INIT}, {@value Ordinal#ORDINAL_INIT}, and {@value Cell#CELL_INIT}
	 */
	public static TimestampedCell TIMESTAMPED_CELL_INIT = new TimestampedCell();
	
	/**
	 * default constructor: with {@value Timestamp#TIMESTAMP_INIT}, {@value Ordinal#ORDINAL_INIT}, and {@value Cell#CELL_INIT}
	 */
	public TimestampedCell() {}
	
	// FIXME to include Ordinal
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
	
	public Ordinal getOrdinal()
	{
		return this.ord;
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
		return Objects.hashCode(this.ts); // , this.cell);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (o instanceof TimestampedCell))
			return false;
		
		TimestampedCell that = (TimestampedCell) o;
		return Objects.equal(this.ts, that.ts); // && Objects.equal(this.cell, that.cell); // uniquely identified by its {@link Timestamp}
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.addValue(this.ts).addValue(this.ord).addValue(this.cell)
				.toString();
	}
}
