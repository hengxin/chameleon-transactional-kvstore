package kvs.component;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 */
public class Timestamp implements Comparable<Timestamp>
{
	/**
	 * Initial value: 0
	 */
	public static Timestamp TIMESTAMP_INIT = new Timestamp();
	
	private long ts = 0L;
	
	public Timestamp() {}
	
	public Timestamp(long ts)
	{
		this.ts = ts;
	}

	@Override
	public int compareTo(Timestamp that)
	{
		return ComparisonChain.start().compare(this.ts, that.ts).result();
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hashCode(this.ts);
	}

	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(! (o instanceof Timestamp))
			return false;
		
		Timestamp that = (Timestamp) o;
		return Objects.equal(this.ts, that.ts);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this).add("ts", this.ts).toString();
	}
}
