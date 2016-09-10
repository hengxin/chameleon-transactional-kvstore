package benchmarking.executor;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hengxin
 * @date 16-9-10
 */
public class ExponentialInterIssueTimeGenerator implements IInterIssueTimeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExponentialInterIssueTimeGenerator.class);

    private final ExponentialDistribution expDist;

    /**
     * @param meanInterval parameter for the exponential distribution;
     *                     in "double"; in milliseconds.
     */
    public ExponentialInterIssueTimeGenerator(final double meanInterval) {
        this.expDist = new ExponentialDistribution(meanInterval);
    }

    @Override
    public long generate() {
        return Math.round(expDist.sample());
    }

}
