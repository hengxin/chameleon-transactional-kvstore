package client.workload.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

import client.workload.operation.IOperationGenerator;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class TransactionGenerator implements ITransactionGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionGenerator.class);

    private final ITransactionSizeGenerator transactionSizeGenerator;
    private final IOperationGenerator opGenerator;

    public TransactionGenerator(final ITransactionSizeGenerator transactionSizeGenerator,
                                final IOperationGenerator opGenerator) {
        this.transactionSizeGenerator = transactionSizeGenerator;
        this.opGenerator = opGenerator;
    }

    @Override
    public Transaction generate() {
        int numberOfOperations = transactionSizeGenerator.generate();

        Transaction tx = new Transaction(numberOfOperations);
        IntStream.range(0, numberOfOperations).forEach(index -> {
            tx.addOp(opGenerator.generate());
        });

        return tx;
    }

}
