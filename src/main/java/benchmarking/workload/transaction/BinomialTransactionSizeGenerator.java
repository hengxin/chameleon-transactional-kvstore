package benchmarking.workload.transaction;

import org.apache.commons.math3.distribution.BinomialDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hengxin
 * @date 16-9-9
 */
public class BinomialTransactionSizeGenerator implements ITransactionSizeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(BinomialTransactionSizeGenerator.class);
    private static final double HUNDRED = 100.0;

    private final BinomialDistribution biDist;

    /**
     * @param maxSize
     * @param prob success probability in {@link BinomialDistribution} (in [0, 100])
     */
    public BinomialTransactionSizeGenerator(final int maxSize, final int prob) {
        this.biDist = new BinomialDistribution(maxSize, prob / HUNDRED);
    }

    @Override
    public int generate() {
        return biDist.sample();
    }

}
