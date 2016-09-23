package benchmarking.workload.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

/**
 * {@link ReadFirstRWRatioOperationTypeGenerator} generates a sequence of
 * {@link OpType}s in which all reads come before all writes.
 *
 * @author hengxin
 * @date 16-9-10
 */
public class ReadFirstRWRatioOperationTypeGenerator implements IOperationTypeGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadFirstRWRatioOperationTypeGenerator.class);

    private final OpType[] opTypeSequence;
    private int index = 0;

    /**
     * @param numberOfOperations    total number of operation types to generate
     * @param rwRatio   ratio of #Reads/#Writes
     */
    public ReadFirstRWRatioOperationTypeGenerator(final int numberOfOperations, final double rwRatio) {
        double writeProb = 1.0 / (rwRatio + 1);
        double readProb = 1.0 - writeProb;

        int numberOfReads = (int) Math.ceil(readProb * numberOfOperations);

        OpType[] types = new OpType[numberOfOperations];
        IntStream.range(0, numberOfReads)
                .forEach(i -> types[i] = OpType.READ);
        IntStream.range(numberOfReads, numberOfOperations)
                .forEach(i -> types[i] = OpType.WRITE);

        opTypeSequence = types;
    }

    @Override
    public OpType generate() {
        return opTypeSequence[index++];
    }

}
