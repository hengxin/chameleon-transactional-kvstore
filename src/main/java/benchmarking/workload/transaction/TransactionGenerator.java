package benchmarking.workload.transaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

import benchmarking.workload.operation.IOperationGenerator;
import benchmarking.workload.rvsi.IRVSISpecificationGenerator;
import client.clientlibrary.rvsi.rvsimanager.RVSISpecificationManager;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class TransactionGenerator implements ITransactionGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionGenerator.class);

    private final ITransactionSizeGenerator transactionSizeGenerator;
    private final IOperationGenerator opGenerator;
    private final IRVSISpecificationGenerator rvsiSpecGenerator;

    public TransactionGenerator(final ITransactionSizeGenerator transactionSizeGenerator,
                                final IOperationGenerator opGenerator,
                                final IRVSISpecificationGenerator rvsiSpecGenerator) {
        this.transactionSizeGenerator = transactionSizeGenerator;
        this.opGenerator = opGenerator;
        this.rvsiSpecGenerator = rvsiSpecGenerator;
    }

    @Override
    public Transaction generate() {
        int numberOfOperations = transactionSizeGenerator.generate();

        Transaction tx = new Transaction(numberOfOperations);
        IntStream.range(0, numberOfOperations).forEach(index ->
                tx.addOp(opGenerator.generate()));

        RVSISpecificationManager rvsiSpecManager = rvsiSpecGenerator.generateRVSISpecManager(tx.getReadOps());
        tx.setRvsiSpecManager(rvsiSpecManager);

        return tx;
    }

}
