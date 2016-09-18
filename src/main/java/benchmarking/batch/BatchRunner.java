package benchmarking.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import benchmarking.statistics.BenchmarkingStatistics;
import benchmarking.statistics.IWorkloadStatistics;
import benchmarking.workload.WorkloadUtil;
import main.benchmarking.BenchmarkingLauncher;

import static benchmarking.workload.WorkloadUtil.WorkloadParams.K1BV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K2FV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K3SV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.MPL;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.RVSI;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.RW_RATIO;
import static conf.SiteConfig.IS_IN_SIMULATION_MODE;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class BatchRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchRunner.class);
    private static final String ALION_SCRIPT =
            "/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/src/main/resources/aliyun/alicript" +
                    "/alion.sh";
    private static final String ALIEAN_SCRIPT =
            "/home/hengxin/idea-projects/chameleon-transactional-kvstore-maven/src/main/resources/aliyun/alicript" +
            "/aliean.sh";

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
        Arrays.stream(batch.getRwRatios()).forEach(rwRatio -> {
            Arrays.stream(batch.getMpls()).forEach(mpl -> {
                Arrays.stream(batch.getRvsiTriples()).forEach(rvsiTriple -> {
                    Properties prop = fillWorkloadProperties(rwRatio, mpl, rvsiTriple);
                    LOGGER.debug("BatchRunner: [{}]", prop.toString());

                    LOGGER.info("#####################################################");
                    LOGGER.info("Batch for [{}:{}:{}] starts.", rwRatio, mpl, rvsiTriple);

                    if (! IS_IN_SIMULATION_MODE)
                        setUp();

                    LOGGER.info("BenchmarkingLauncher starts.");
                    IWorkloadStatistics workloadStat = new BenchmarkingLauncher(prop,
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
                });
            });
        });
    }

    private Properties fillWorkloadProperties(double rwRatio, int mpl, RVSITriple rvsiTriple) {
        Properties prop = WorkloadUtil.DEFAULT_WORKLOAD_PROPERTIES;

        prop.setProperty(MPL.param(), String.valueOf(mpl));
        prop.setProperty(RW_RATIO.param(), String.valueOf(rwRatio));

        prop.setProperty(RVSI.param(), rvsiTriple.rvsiParamVal());
        prop.setProperty(K1BV.param(), String.valueOf(rvsiTriple.getK1()));
        prop.setProperty(K2FV.param(), String.valueOf(rvsiTriple.getK2()));
        prop.setProperty(K3SV.param(), String .valueOf(rvsiTriple.getK3()));

        return prop;
    }

    private void setUp() {
        LOGGER.info("[{}] starts.", ALION_SCRIPT);
        try {
            Process alionProc = new ProcessBuilder(ALION_SCRIPT).start();
            alionProc.waitFor();
        } catch (IOException | InterruptedException ioie) {
            ioie.printStackTrace();
        }
        LOGGER.info("[{}] ends.", ALION_SCRIPT);
    }

    private void destroy() {
        LOGGER.info("[{}] starts.", ALIEAN_SCRIPT);
        try {
            Process alieanProc = new ProcessBuilder(ALIEAN_SCRIPT).start();
            alieanProc.waitFor();
        } catch (IOException | InterruptedException ioie) {
            ioie.printStackTrace();
        }
        LOGGER.info("[{}] ends.", ALIEAN_SCRIPT);
    }

}
