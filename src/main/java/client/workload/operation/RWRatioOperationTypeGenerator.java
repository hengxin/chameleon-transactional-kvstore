package client.workload.operation;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link RWRatioOperationTypeGenerator}
 * generates a sequence of operation types (read (0) or write (1))
 * based on the r/w ratio.
 *
 * @author hengxin
 * @date 16-9-8
 */
public class RWRatioOperationTypeGenerator implements IOperationTypeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(RWRatioOperationTypeGenerator.class);

    private final int rwRatio;

    public RWRatioOperationTypeGenerator(int rwRatio) {
        this.rwRatio = rwRatio;
    }

    @Override
    public OpType generate() {
        double writeProb = 1.0 / (rwRatio + 1);
        double readProb = 1.0 - writeProb;

        int[] types = new int[] {0, 1};
        double[] probs = new double[] {readProb, writeProb};
        EnumeratedIntegerDistribution dist = new EnumeratedIntegerDistribution(types, probs);
        return dist.sample() == 0 ? OpType.READ : OpType.WRITE;
    }

}
