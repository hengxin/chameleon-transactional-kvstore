package main.benchmarking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmarking.batch.RVSITriple;
import benchmarking.statistics.BenchmarkingStatistics;
import benchmarking.statistics.AbstractWorkloadStatistics;

/**
 * @author hengxin
 * @date 16-9-18
 */
public class BenchmarkingLauncherScriptMainTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkingLauncherScriptMainTest.class);

    public static void main(String[] args) {
        String rwRatio = args[0];
        String mpl = args[1];
        String rvsi = args[2];

        String siteProperties = args[3];
        String cfProperties = args[4];
        String toProperties = args[5];

        LOGGER.info("#####################################################");
        LOGGER.info("Batch for [{}:{}:{}] begins.", rwRatio, mpl, rvsi);

        LOGGER.info("BenchmarkingLauncher starts.");
        AbstractWorkloadStatistics workloadStat = new BenchmarkingLauncher(rwRatio, mpl, rvsi,
                siteProperties, cfProperties, toProperties)
                .run();
        LOGGER.info("BenchmarkingLauncher ends.");

        BenchmarkingStatistics benchmarkingStat = new BenchmarkingStatistics(Double.parseDouble(rwRatio),
                Integer.parseInt(mpl), new RVSITriple(rvsi),
                workloadStat);
        LOGGER.info("BatchRunner: [{}].", benchmarkingStat.briefReport());

        LOGGER.info("Batch for [{}:{}:{}] ends.", rwRatio, mpl, rvsi);
        LOGGER.info("#####################################################");
    }

}
