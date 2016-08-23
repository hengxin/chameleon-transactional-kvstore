package kvs.compound;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

import kvs.component.Cell;
import kvs.component.Ordinal;
import kvs.component.Timestamp;

/**
 * A {@link TimestampedCell} is a {@link Cell} associated with a {@link Timestamp} (beside its {@link Ordinal}).
 * 
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * @implNote
 * 	A {@link TimestampedCell} is uniquely identified by its {@link #ts} ({@link Timestamp}) field.
 *  See its {@link #compareTo(ITimestampedCell)}, {@link #hashCode()}, and {@link #equals(Object)}.
 *  <p> Builder design pattern???
 */
@Immutable
@ThreadSafe
public class TimestampedCell implements ITimestampedCell {
	private static final long serialVersionUID = -764314996680845231L;

	private final Timestamp ts; 
	private final Ordinal ord; 
	private final Cell cell; 
	
	/**
	 * Initial value: {@link Timestamp#TIMESTAMP_INIT}, {@link Ordinal#ORDINAL_INIT}, and {@link Cell#CELL_INIT}
	 */
	public final static TimestampedCell TIMESTAMPED_CELL_INIT = new TimestampedCell();
	
	/**
	 * Default constructor with {@link Timestamp#TIMESTAMP_INIT}, {@link Ordinal#ORDINAL_INIT},
     * and {@link Cell#CELL_INIT}
	 */
	public TimestampedCell() {
		ts = Timestamp.TIMESTAMP_INIT;
		ord = Ordinal.ORDINAL_INIT;
		cell = Cell.CELL_INIT;
	}
	
	public TimestampedCell(Timestamp ts, Ordinal ord, Cell c) {
		this.ts = ts;
		this.ord = ord;
		this.cell = c;
	}

	/**
	 * Constructor with the {@link #ts} and {@link #cell}, if {@link #ord} is not relevant.
	 * @param ts {@link Timestamp}
	 * @param c {@link Cell}
	 */
	public TimestampedCell(Timestamp ts, Cell c) {
		this.ts = ts;
		this.cell = c;
		this.ord = Ordinal.ORDINAL_INIT;
	}
	
	/**
	 * Constructor with only {@link #cell}, if {@link #ts} and {@link #ord} are not relevant. 
	 * @param cell
	 */
	public TimestampedCell(Cell cell) {
		this.cell = cell;
		this.ts = Timestamp.TIMESTAMP_INIT;
		this.ord = Ordinal.ORDINAL_INIT;
	}

	public static TimestampedCell replaceTimestamp(Timestamp ts, ITimestampedCell ts_cell) {
		return new TimestampedCell(ts, ts_cell.getOrdinal(), ts_cell.getCell());
	}
	
	@Override
	public Timestamp getTS() { return ts; }
	public Ordinal getOrdinal() { return ord; }
	
	@Override
	public Cell getCell() { return cell; }
	
	/**
	 * Compared by their {@link #ts} (of class {@link Timestamp}).
	 */
	@Override
	public int compareTo(ITimestampedCell that) {
		return ComparisonChain.start().compare(this.ts, ((TimestampedCell) that).ts).result();
	}

	/**
	 * Only hashCode the {@link #ts} field. 
	 * Consistent with {@link #compareTo(ITimestampedCell)}.
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.ts); 
	}
	
	/**
	 * Only check the {@link #ts} field.
	 * Consistent with {@link #compareTo(ITimestampedCell)}.
	 */
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(! (o instanceof TimestampedCell))
			return false;
		
		TimestampedCell that = (TimestampedCell) o;
		return Objects.equal(this.ts, that.ts);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(this.ts).addValue(this.ord).addValue(this.cell)
				.toString();
	}

}
