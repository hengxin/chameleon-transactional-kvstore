package main.benchmarking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmarking.statistics.AbstractWorkloadStatistics;

/**
 * Run benchmarking.
 *
 * @author hengxin
 * @date 16-9-11
 * @see BenchmarkingLauncher
 */
public class BenchmarkingMainTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkingMainTest.class);

    public static void main(String[] args) {
        String workloadProperties = args[0];
        String siteProperties = args[1];
        String cfProperties = args[2];
        String toProperties = args[3];

        LOGGER.info("########## Benchmarking Main Test Begins!!! ##########");
        AbstractWorkloadStatistics workloadStat =
                new BenchmarkingLauncher(workloadProperties, siteProperties, cfProperties, toProperties)
                        .run();

        if (workloadStat != null)
            LOGGER.info(workloadStat.briefReport());
        LOGGER.info("########## Benchmarking Main Test Finished!!! ##########");
    }

}
