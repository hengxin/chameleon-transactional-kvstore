package twopc.timing;

import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple centralized timestamp oracle.
 * The timestamp sequence starts from 0.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
@ThreadSafe
public enum CentralizedTimestampOracle implements ITimestampOracle {
    INSTANCE;
	private final AtomicInteger ts = new AtomicInteger();
	
	@Override
	public int get() { return ts.getAndIncrement(); }

    @Override
    public void export() {

    }

    @Override
    public void reclaim() {

    }

}
