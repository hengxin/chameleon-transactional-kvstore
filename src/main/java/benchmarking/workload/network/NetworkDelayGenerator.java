package benchmarking.workload.network;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import benchmarking.workload.WorkloadUtil.WorkloadParams;

/**
 * {@link NetworkDelayGenerator} simulates the intra/inter-datacenter
 * network delays.
 *
 * Created by hengxin on 16-9-26.
 * TODO: refactor to avoid duplicate code among methods.
 */
public class NetworkDelayGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkDelayGenerator.class);

    private static final NormalDistribution TWO_PC_DELAY_NORMAL_DIST = new NormalDistribution(
            Integer.parseInt(WorkloadParams.TWO_PC_DELAY.val()), 1);
    private static final NormalDistribution REPLICATION_DELAY_NORMAL_DIST = new NormalDistribution(
            Integer.parseInt(WorkloadParams.REPILCATION_DELAY.val()), 1);
    private static final NormalDistribution ISSUE_NORMAL_DIST = new NormalDistribution(
            Integer.parseInt(WorkloadParams.ISSUE_DELAY.val()), 1);
    private static final NormalDistribution TIME_ORACLE_NORMAL_DIST = new NormalDistribution(
            Integer.parseInt(WorkloadParams.TIME_ORACLE_DELAY.val()), 1);

    /**
     * Simulating replication communication by injecting artificial delays.
     * It appears when a master propagates committed transactions to its slaves.
     */
    public static void simulateReplicationComm() {
        if (Boolean.parseBoolean(WorkloadParams.SIMULATION.val()))
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(REPLICATION_DELAY_NORMAL_DIST.sample()));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
    }

    /**
     * Simulating 2PC communication by injecting artificial delays.
     * It appears when a 2PC coordinator executes the 2PC protocols,
     * including both the PREPARE phase and the COMMIT phase.
     */
    public static void simulate2PCComm() {
        if (Boolean.parseBoolean(WorkloadParams.SIMULATION.val()))
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(TWO_PC_DELAY_NORMAL_DIST.sample()));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
    }

    /**
     * Simulating transaction operations issuing communication by injecting artificial delays.
     * It appears when clients issue transaction operations.
     */
    public static void simulateClientIssueComm() {
        if (Boolean.parseBoolean(WorkloadParams.SIMULATION.val()))
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(ISSUE_NORMAL_DIST.sample()));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
    }

    /**
     * Simulating time oracle contacting communication by injecting artificial delays.
     * It appears when a 2PC coordinator contacts the time oracle.
     */
    public static void simulateTimeOracleComm() {
        if (Boolean.parseBoolean(WorkloadParams.SIMULATION.val()))
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(TIME_ORACLE_NORMAL_DIST.sample()));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
    }

}
