package twopc.timing;

import java.util.concurrent.atomic.AtomicInteger;

import net.jcip.annotations.ThreadSafe;

/**
 * A simple centralized timestamp oracle.
 * The timestamp sequence starts from 0.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
@ThreadSafe
public final class CentralizedTimestampOracle implements ITimestampOracle
{
	private final AtomicInteger ts = new AtomicInteger();
	
	@Override
	public int get()
	{
		return this.ts.getAndIncrement();
	}

}
