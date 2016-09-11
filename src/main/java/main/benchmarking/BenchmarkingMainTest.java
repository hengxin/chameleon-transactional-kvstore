package main.benchmarking;

/**
 * @author hengxin
 * @date 16-9-11
 */
public class BenchmarkingMainTest {
    public static void main(String[] args) {
        String workloadProperties = args[0];
        String siteProperties = args[1];
        String cfProperties = args[2];
        String toProperties = args[3];

        new BenchmarkingLauncher(workloadProperties, siteProperties, cfProperties, toProperties)
                .run();
    }

}
