package kvs.table;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 */
public class Timestamp implements Comparable<Timestamp>
{
	private long ts = 0L;

	@Override
	public int compareTo(Timestamp o)
	{
		return (int) (this.ts - o.ts);
	}

}
