package client.workload.transaction;

import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hengxin
 * @date 16-9-8
 */
public class UniformTransactionSizeGenerator implements ITransactionSizeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(UniformTransactionSizeGenerator.class);

    private final UniformIntegerDistribution uniformDist;

    public UniformTransactionSizeGenerator(int maxSize) {
        uniformDist = new UniformIntegerDistribution(0, maxSize - 1);
    }

    @Override
    public int generate() {
        return uniformDist.sample();
    }

}
