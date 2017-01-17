package main.benchmarking;

import benchmarking.batch.BatchRunner;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class BatchRunnerMainTest {
    public static void main(String[] args) {
        String batchProperties = args[0];
        String siteProperties = args[1];
        String cfProperties = args[2];
        String toProperties = args[3];

        new BatchRunner(batchProperties, siteProperties, cfProperties, toProperties)
                .run();
    }
}
