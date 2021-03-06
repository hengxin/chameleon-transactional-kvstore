package kvs.component;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

/**
 * @author hengxin
 * @date Created on 11-10-2015
 */
@Immutable
public class Timestamp implements Comparable<Timestamp>, Serializable {
	private static final long serialVersionUID = -4196523878242377170L;

	public static final Timestamp TIMESTAMP_INIT = new Timestamp(0);
    public static final Timestamp TIMESTAMP_MAX = new Timestamp(Long.MAX_VALUE);

    private long ts = 0L;
	
	public Timestamp(long ts) { this.ts = ts; }
    public long getTs() { return ts; }

	@Override
	public int compareTo(@NotNull Timestamp that) {
		return ComparisonChain.start().compare(this.ts, that.ts).result();
	}
	
	@Override
	public int hashCode() { return Objects.hashCode(ts); }

	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
        if (o == null || !(o.getClass() == this.getClass()))
            return false;

        Timestamp that = (Timestamp) o;
		return Objects.equal(this.ts, that.ts);
	}
	
    @Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("ts", ts)
				.toString();
	}

}
