package timing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import conf.SiteConfig;
import membership.site.Member;
import rmi.IRMI;
import rmi.RMIUtil;
import utils.PropertiesUtil;

/**
 * A simple <it>centralized</it> timestamp oracle.
 * The timestamp sequence starts from 0.
 *
 * To make the transaction commit phase atomic in 2PC protocol,
 * It is necessary to prevent the events of getting new start timestamps
 * between the time some transaction gets its commit timestamp and the time it actually commits.
 *
 * @see <a ref="https://github.com/hengxin/chameleon-transactional-kvstore/issues/37">ISSUE #37 Making the transaction commit phase atomic in 2PC protocol</a>
 *
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public class CentralizedTimestampOracle implements ITimestampOracle, IRMI {
    private static final Logger LOGGER = LoggerFactory.getLogger(CentralizedTimestampOracle.class);

    private final AtomicInteger ts = new AtomicInteger();
    private Member self;

    private static final int MAX_THREADS = 1_000;
    private final Semaphore semaphore = new Semaphore(MAX_THREADS);

    public CentralizedTimestampOracle() { this(SiteConfig.DEFAULT_TO_PROPERTIES); }

    public CentralizedTimestampOracle(String cfProperties) {
        try {
            Properties prop = PropertiesUtil.load(cfProperties);
            Set<String> idStrs = prop.stringPropertyNames();
            String idStr = idStrs.toArray(new String[idStrs.size()])[0];
            self = Member.parseMember(prop.getProperty(idStr)).get();
            export();

            LOGGER.info("[{}:{}] successfully launched.",
                    CentralizedTimestampOracle.class.getSimpleName(), self);
        } catch (IOException ioe) {
            LOGGER.error("Failed to parse [{}] from properties [{}]", this.getClass().getSimpleName(), cfProperties);
        }
    }

	private int getTs() {
	    return ts.getAndIncrement();
	}

    @Override
    public void unlockSts()
            throws RemoteException, InterruptedException {
        semaphore.release();
    }

    /**
     * @return  start timestamp for some transaction
     * @throws RemoteException
     * @throws InterruptedException
     *
     * @implNote The only constraint is that {@link #getSts()} is blocked
     *  if some threads are in {@link #lockStsAndThenGetCts()}.
     *  The locking strategy employed here is coarse-grained and conservative,
     *  hurting the system performance.
     */
    @Override
    public int getSts()
            throws RemoteException, InterruptedException {
        semaphore.acquire(MAX_THREADS);
        int sts = getTs();
        semaphore.release(MAX_THREADS);
        return sts;
    }

    @Override
    public int lockStsAndThenGetCts()
            throws RemoteException, InterruptedException {
        semaphore.acquire();
        return getTs();
    }

    @Override
    public void export() { RMIUtil.export(this, self.getHost(), self.getPort(), self.getRmiRegistryName()); }

    @Override
    public void reclaim() {

    }

}
