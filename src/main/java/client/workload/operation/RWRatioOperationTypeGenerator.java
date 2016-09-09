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

    private final EnumeratedIntegerDistribution eiDist;

    public RWRatioOperationTypeGenerator(int rwRatio) {
        double writeProb = 1.0 / (rwRatio + 1);
        double readProb = 1.0 - writeProb;

        int[] types = new int[] {0, 1};
        double[] probs = new double[] {readProb, writeProb};
        eiDist = new EnumeratedIntegerDistribution(types, probs);
    }

    @Override
    public OpType generate() {
        return eiDist.sample() == 0 ? OpType.READ : OpType.WRITE;
    }

}
