package main.benchmarking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hengxin
 * @date 16-9-11
 */
public class BenchmarkingMainTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkingMainTest.class);

    public static void main(String[] args) {
        String workloadProperties = args[0];
        String siteProperties = args[1];
        String cfProperties = args[2];
        String toProperties = args[3];

        new BenchmarkingLauncher(workloadProperties, siteProperties, cfProperties, toProperties)
                .run();


        LOGGER.info("Benchmarking Main Test Finished!!!");
    }

}
