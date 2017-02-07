package main.benchmarking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import benchmarking.statistics.AbstractWorkloadStatistics;
import utils.PropertiesUtil;
import utils.ScriptUtil;

import static benchmarking.workload.WorkloadUtil.WorkloadParams.ISSUE_DELAY;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K1BV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K2FV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K3SV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.REPILCATION_DELAY;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.RW_RATIO;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.SIMULATION;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.SIZE_OF_KEYSPACE;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.TIME_ORACLE_DELAY;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.TWO_PC_DELAY;

/**
 * Run benchmarking in batch.
 *
 * @author hengxin
 * @date 16-12-2016
 * @see BenchmarkingLauncher
 */
public class BenchmarkingBatchMainTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkingBatchMainTest.class);
    private static ExecutorService exec = Executors.newCachedThreadPool();

    private static final String JAR_DIR = "/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/target/";

    private static String[] chameleonProcesses = new String[] { "slave.jar", "master.jar", "cf.jar", "to.jar" };

    private static void startChameleon() throws InterruptedException {
        String[] slaveIds = new String[] { "00", "01", "10", "11", "20", "21" };
        for (String slaveId : slaveIds) {
            exec.submit(() ->
                    ScriptUtil.exec(new String[]{"java", "-cp", JAR_DIR + "slave.jar", "main.SlaveMainTest",
                            "slave/site-slave" + slaveId + ".properties",
                            "messaging/socket/sp" + slaveId + ".properties"}));
            TimeUnit.SECONDS.sleep(5);
        }

        String[] masterIds = new String[] { "0", "1", "2" };
        for (String masterId : masterIds) {
            exec.submit(() ->
                    ScriptUtil.exec(new String[]{"java", "-cp", JAR_DIR + "master.jar", "main.MasterMainTest",
                            "master/site-master" + masterId + ".properties",
                            "messaging/socket/sa" + masterId + ".properties"}));
            TimeUnit.SECONDS.sleep(5);
        }

        for (String masterId : masterIds) {
            exec.submit(() ->
                    ScriptUtil.exec(new String[]{"java", "-cp", JAR_DIR + "cf.jar", "main.CoordinatorFactoryMainTest",
                            "membership/coordinator/cf" + masterId + ".properties",
                            "timing/to.properties"}));
            TimeUnit.SECONDS.sleep(5);
        }

        exec.submit(() ->
                ScriptUtil.exec(new String[]{"java", "-cp", JAR_DIR + "to.jar", "main.CentralizedTimestampOracleMainTest",
                        "timing/to.properties"}));
    }

    private static void destroyChameleon() throws IOException {
        ScriptUtil.exec(new String[] { Paths.get(".").toAbsolutePath().normalize()
                .toAbsolutePath() + "/src/main/java/utils/kill.sh" });
    }

    public static void main(String[] args) throws IOException {
        String workloadProperties = args[0];
        String siteProperties = args[1];
        String cfProperties = args[2];
        String toProperties = args[3];

        Properties workProp = PropertiesUtil.load(workloadProperties);

        // set simulation mode
        workProp.setProperty(SIMULATION.param(), "true");

        String[] sizesOfKeyspace = new String[]{"4"}; // 3, 4, 5, 7
        String[] rwRatios = new String[]{"0.5"}; // 0.5, 1, 4, 9
        String[] issueDelays = new String[]{"4", "5", "10", "15", "20"};  // 2, 3, 4, 5, 10, 15, 20
        String[] otherDelays = new String[]{"10", "20", "50", "100"};   // 10, 20, 50, 100

        // set k1, k2, and k3
        workProp.setProperty(K1BV.param(), "2");
        workProp.setProperty(K2FV.param(), "0");
        workProp.setProperty(K3SV.param(), "1");

        for (String sizeOfKeyspace : sizesOfKeyspace)
            for (String rwRatio : rwRatios)
                for (String issueDelay : issueDelays)
                    for (String otherDelay : otherDelays) {
                        LOGGER.info("sizeOfKeyspace [{}]; rwRatio [{}]; issueDelay [{}]; otherDelay [{}].",
                                sizeOfKeyspace, rwRatio, issueDelay, otherDelay);

                        // set workload parameters
                        workProp.setProperty(SIZE_OF_KEYSPACE.param(), sizeOfKeyspace);
                        workProp.setProperty(RW_RATIO.param(), rwRatio);
                        workProp.setProperty(ISSUE_DELAY.param(), issueDelay);
                        workProp.setProperty(TWO_PC_DELAY.param(), otherDelay);
                        workProp.setProperty(REPILCATION_DELAY.param(), otherDelay);
                        workProp.setProperty(TIME_ORACLE_DELAY.param(), otherDelay);

                        // run benchmarking with workload parameters above
                        try {
                            startChameleon();
                            TimeUnit.SECONDS.sleep(10);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }

                        LOGGER.info("########## Benchmarking Batch Main Test Begins!!! ##########");
                        AbstractWorkloadStatistics workloadStat =
                                new BenchmarkingLauncher(workProp, siteProperties, cfProperties, toProperties)
                                        .run();

                        if (workloadStat != null)
                            LOGGER.info(workloadStat.briefReport());
                        LOGGER.info("########## Benchmarking Batch Main Test Finished!!! ##########");

                        destroyChameleon();
                    }
    }
}
