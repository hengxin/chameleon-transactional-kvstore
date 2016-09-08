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

    private final int maxSize;
    private final UniformIntegerDistribution uniformDist;

    public UniformTransactionSizeGenerator(int maxSize) {
        this.maxSize = maxSize;
        uniformDist = new UniformIntegerDistribution(0, maxSize - 1);
    }

    /**
     * @implSpec It always return {@code true}.
     * Therefore don't use {@code while (hasNext())}.
     * @return {@code true} (always)
     */
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        return uniformDist.sample();
    }

}
