package kvs.compound;

import java.io.Serializable;

import kvs.component.Cell;
import kvs.component.Ordinal;
import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * <p> Interface for timestamped-cells, {@link Cell}s associated with {@link Timestamp}s.
 */
public interface ITimestampedCell extends Comparable<ITimestampedCell>, Serializable
{
	public Timestamp getTS();
	public Ordinal getOrdinal();
	public Cell getCell();
}
