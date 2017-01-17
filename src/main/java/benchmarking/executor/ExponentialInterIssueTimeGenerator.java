package benchmarking.executor;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hengxin
 * @date 16-9-10
 */
public class ExponentialInterIssueTimeGenerator implements IInterIssueTimeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExponentialInterIssueTimeGenerator.class);

    @NotNull private final ExponentialDistribution expDist;
    private final int minInterval;
    private final int maxInterval;

    /**
     * @param meanInterval parameter for the exponential distribution;
     *                     in "double"; in milliseconds.
     */
    public ExponentialInterIssueTimeGenerator(final int minInterval, final int maxInterval,
                                              final int meanInterval) {
        this.minInterval = minInterval;
        this.maxInterval = maxInterval;
        expDist = new ExponentialDistribution(meanInterval);
    }

    @Override
    public long generate() {
        long interval = Math.round(expDist.sample()) + minInterval;
        return interval > maxInterval ? maxInterval : interval;
    }

}
