package benchmarking.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmarking.executor.ExeBenchmarking;

/**
 * @author hengxin
 * @date 16-9-17
 */
public abstract class AbstractBatchRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBatchRunner.class);
    private final IBenchmarkingGenerator batchGenerator;

    protected AbstractBatchRunner(IBenchmarkingGenerator batchGenerator) { this.batchGenerator = batchGenerator; }

    public void run() {
        while (batchGenerator.hasNext()) {
            before();

            ExeBenchmarking exeBenchmarking = batchGenerator.next();

            after();
        }
    }

    abstract boolean before();
    abstract void exec();
    abstract boolean after();
}
