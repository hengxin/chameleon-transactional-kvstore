package benchmarking.batch;

import benchmarking.executor.ExeBenchmarking;

/**
 * @author hengxin
 * @date 16-9-17
 */
public class BenchmarkingGeneratorFromProperties implements IBenchmarkingGenerator {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public ExeBenchmarking next() {
        return null;
    }
}
