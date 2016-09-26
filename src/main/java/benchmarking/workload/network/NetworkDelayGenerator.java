package benchmarking.workload.network;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import conf.SiteConfig;

/**
 * {@link NetworkDelayGenerator} simulates the intra/inter-datacenter
 * network delays.
 *
 * Created by hengxin on 16-9-26.
 */
public class NetworkDelayGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkDelayGenerator.class);

    private static int intraDCDelay = 5;
    private static int interDCDelay = 40;
    private static final NormalDistribution INTRA_DC_NORMAL_DIST = new NormalDistribution(intraDCDelay, 1);
    private static final NormalDistribution INTER_DC_NORMAL_DIST = new NormalDistribution(interDCDelay, 1);

    public static void simulateInterDCComm() {
        if (SiteConfig.IS_IN_SIMULATION_MODE)
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(INTER_DC_NORMAL_DIST.sample()));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
    }

    public static void simulateIntraDCComm() {
        if (SiteConfig.IS_IN_SIMULATION_MODE)
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(INTRA_DC_NORMAL_DIST.sample()));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
    }

}
