package client.workload;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmarking.workload.overall.IWorkloadGenerator;
import benchmarking.workload.overall.Workload;
import benchmarking.workload.overall.WorkloadGeneratorFromProperties;

/**
 * @author hengxin
 * @date 16-9-8
 */
public class WorkloadGeneratorFromPropertiesTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadGeneratorFromPropertiesTest.class);

    IWorkloadGenerator workloadGenerator = new WorkloadGeneratorFromProperties("client/workload.properties");

    @Test
    public void generate() throws Exception {
        Workload workload = workloadGenerator.generate();

        LOGGER.info(workload.toString());
    }

}