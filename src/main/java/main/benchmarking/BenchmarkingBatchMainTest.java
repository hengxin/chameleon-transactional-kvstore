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

        // 3, 5 (fixed now), 7
        String[] sizesOfKeyspace = new String[]{"5"};
        // 0.5, 1, 4
        String[] rwRatios = new String[]{"4"};
        // (2, deprecated) 5 (done), 8 (done), 10 (done), 12, 15, 18, 20
//        String[] issueDelays = new String[]{"15", "18", "20"};
        String[] issueDelays = new String[]{"10"};

        // 5, 10, 15, 20, 30
        String[] replicationDelays = new String[]{"5"};
        // 10, 20, 30, 40, 50, 100
        String[] twopcDelays = new String[]{"20"};
        // k1, k2, and k3: 100, 110, 111, 200, 201, 211
//        String[] rvsis = new String[]{"100", "110", "111", "200", "201", "211"};
        String[] rvsis = new String[]{"211"};

        // missing
        // issueDelay = 10, rvsi: 211

        for (String sizeOfKeyspace : sizesOfKeyspace)
            for (String rwRatio : rwRatios)
                for (String issueDelay : issueDelays)
                    for (String replicationDelay : replicationDelays)
                        for (String twopcDelay : twopcDelays)
                            for (String rvsi : rvsis)
                        {
                            LOGGER.info("sizeOfKeyspace [{}]; rwRatio [{}]; "
                                    + "issueDelay [{}]; replicationDelay [{}]; twopcDelay [{}]; rvsi [{}]",
                                    sizeOfKeyspace, rwRatio, issueDelay, replicationDelay, twopcDelay, rvsi);

                            // set workload parameters
                            workProp.setProperty(SIZE_OF_KEYSPACE.param(), sizeOfKeyspace);
//                            workProp.setProperty(NUMBER_OF_TRANSACTIONS.param(), "800");
                            workProp.setProperty(RW_RATIO.param(), rwRatio);
                            workProp.setProperty(ISSUE_DELAY.param(), issueDelay);
                            workProp.setProperty(TWO_PC_DELAY.param(), twopcDelay);
                            workProp.setProperty(REPILCATION_DELAY.param(), replicationDelay);
                            workProp.setProperty(TIME_ORACLE_DELAY.param(), twopcDelay);

                            workProp.setProperty(K1BV.param(), String.valueOf(rvsi.charAt(0)));
                            workProp.setProperty(K2FV.param(), String.valueOf(rvsi.charAt(1)));
                            workProp.setProperty(K3SV.param(), String.valueOf(rvsi.charAt(2)));

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
