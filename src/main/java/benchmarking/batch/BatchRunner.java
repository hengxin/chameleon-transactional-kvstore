package benchmarking.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import benchmarking.statistics.BenchmarkingStatistics;
import benchmarking.statistics.IWorkloadStatistics;
import main.benchmarking.BenchmarkingLauncher;
import util.ScriptUtil;

import static conf.SiteConfig.IS_IN_SIMULATION_MODE;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class BatchRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchRunner.class);
    private static ExecutorService exec = Executors.newCachedThreadPool();

    private static final String ALION_SCRIPT =
    "/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/src/main/resources/aliyun/alicript/alion.sh";

    private static final String ALIEAN_SCRIPT =
    "/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/src/main/resources/aliyun/alicript/aliean.sh";

    private final Batch batch;
    private final String siteProperties;
    private final String cfProperties;
    private final String toProperties;

    public BatchRunner(final String batchProperties,
                       final String siteProperties, final String cfProperties, final String toProperties) {
        this.batch = new Batch(batchProperties);

        this.siteProperties = siteProperties;
        this.cfProperties = cfProperties;
        this.toProperties = toProperties;
    }

    public BatchRunner(Batch batch,
                       final String siteProperties, final String cfProperties, final String toProperties) {
        this.batch = batch;
        this.siteProperties = siteProperties;
        this.cfProperties = cfProperties;
        this.toProperties = toProperties;
    }

    public void run() {
//        Arrays.stream(batch.getRwRatios()).forEachOrdered(rwRatio -> {
//            Arrays.stream(batch.getMpls()).forEachOrdered(mpl -> {
//                Arrays.stream(batch.getRvsiTriples()).forEachOrdered(rvsiTriple -> {
        for (double rwRatio : batch.getRwRatios()) {
            for (int mpl : batch.getMpls()) {
                for (RVSITriple rvsiTriple : batch.getRvsiTriples()) {

                    LOGGER.info("#####################################################");
                    LOGGER.info("Batch for [{}:{}:{}] starts.", rwRatio, mpl, rvsiTriple);

                    if (! IS_IN_SIMULATION_MODE)
                        setUp();

                    LOGGER.info("BenchmarkingLauncher starts.");
                    IWorkloadStatistics workloadStat = new BenchmarkingLauncher(rwRatio, mpl, rvsiTriple,
                            siteProperties, cfProperties, toProperties)
                            .run();
                    LOGGER.info("BenchmarkingLauncher ends.");

                    BenchmarkingStatistics benchmarkingStat = new BenchmarkingStatistics(rwRatio, mpl, rvsiTriple,
                            workloadStat);
                    LOGGER.info("BatchRunner: [{}].", benchmarkingStat.briefReport());

                    if (! IS_IN_SIMULATION_MODE)
                        destroy();

                    LOGGER.info("Batch for [{}:{}:{}] ends.", rwRatio, mpl, rvsiTriple);
                    LOGGER.info("#####################################################");
                }
            }
        }
    }

    // Process alionProc = new ProcessBuilder().inheritIO().command(ALION_SCRIPT).start();
    private void setUp() {
        LOGGER.info("[{}] starts.", ALION_SCRIPT);

        ScriptUtil.exec(new String[] { ALION_SCRIPT } );

        try {
            TimeUnit.MINUTES.sleep(12);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        LOGGER.info("[{}] ends.", ALION_SCRIPT);
    }

    private void destroy() {
        LOGGER.info("[{}] starts.", ALIEAN_SCRIPT);

        ScriptUtil.exec(new String[] { ALIEAN_SCRIPT} );

        LOGGER.info("Pass the alieanProc.");
        try {
            TimeUnit.MINUTES.sleep(2);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        LOGGER.info("[{}] ends.", ALIEAN_SCRIPT);
    }

}
