package kvs.component;

import java.io.Serializable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import net.jcip.annotations.Immutable;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 */
@Immutable
public class Timestamp implements Comparable<Timestamp>, Serializable
{
	private static final long serialVersionUID = -4196523878242377170L;

	public final static Timestamp TIMESTAMP_INIT = new Timestamp(0);
	public final static Timestamp TIMESTAMP_ERROR = new Timestamp(-1);
	
	private long ts = 0L;
	
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
		if(o == null)
			return false;
		if(! (o.getClass() == this.getClass()))
			return false;
		
		Timestamp that = (Timestamp) o;
		return Objects.equal(this.ts, that.ts);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("ts", this.ts)
				.toString();
	}
}
