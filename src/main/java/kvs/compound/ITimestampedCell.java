package kvs.compound;

import kvs.component.Cell;
import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * Interface for timestamped-cells, {@link Cell}s associated with {@link Timestamp}s.
 */
public interface ITimestampedCell extends Comparable<ITimestampedCell>
{
	public Timestamp getTS();
	public Cell getCell();
}
