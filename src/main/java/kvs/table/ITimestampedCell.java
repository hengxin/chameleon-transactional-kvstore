package kvs.table;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 * 
 * Interface for updating and getting {@link Cell} with versions.
 */
public interface ITimestampedCell
{
	/**
	 * update this {@link Cell} to @param c, associated with {@link Timestamp} ts 
	 * @param ts {@link Timestamp}
	 * @param c {@link Cell}
	 */
	public void update(Timestamp ts, Cell c);
	/**
	 * get the latest preceding {@link Cell} with smaller {@link Timestamp} than @param ts 
	 * @param ts {@link Timestamp}
	 * @return a {@link Cell}
	 */
	public Cell getLatest(Timestamp ts);
}
