package benchmarking.workload.transaction;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

import benchmarking.workload.keyspace.Key;
import benchmarking.workload.operation.IKeyGenerator;
import benchmarking.workload.operation.IOperationTypeGenerator;
import benchmarking.workload.operation.IOperationTypeGenerator.OpType;
import benchmarking.workload.operation.ReadFirstRWRatioOperationTypeGenerator;
import benchmarking.workload.operation.ReadOperation;
import benchmarking.workload.operation.WithPrefixAndSequenceValueGenerator;
import benchmarking.workload.operation.WriteOperation;
import benchmarking.workload.rvsi.IRVSISpecificationGenerator;
import client.clientlibrary.rvsi.rvsimanager.RVSISpecificationManager;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class TransactionGenerator implements ITransactionGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionGenerator.class);

    private final ITransactionSizeGenerator transactionSizeGenerator;
    private final double rwRatio;
    private final IKeyGenerator rowKeyGenerator;
    private final IKeyGenerator columnKeyGenerator;
    private final IRVSISpecificationGenerator rvsiSpecGenerator;

    public TransactionGenerator(@NotNull final ITransactionSizeGenerator transactionSizeGenerator,
                                final double rwRatio,
                                @NotNull final IKeyGenerator rowKeyGenerator,
                                @NotNull final IKeyGenerator columnKeyGenerator,
                                @NotNull final IRVSISpecificationGenerator rvsiSpecGenerator) {
        this.transactionSizeGenerator = transactionSizeGenerator;
        this.rwRatio = rwRatio;
        this.rowKeyGenerator = rowKeyGenerator;
        this.columnKeyGenerator = columnKeyGenerator;
        this.rvsiSpecGenerator = rvsiSpecGenerator;
    }

    @NotNull
    @Override
    public Transaction generate() {
        int numberOfOperations = transactionSizeGenerator.generate();

        IOperationTypeGenerator opTypeGenerator = new ReadFirstRWRatioOperationTypeGenerator
                (numberOfOperations, rwRatio);

        IKeysTransactionFilter readSetFilter = new DistinctKeysTransactionFilter();
        IKeysTransactionFilter writeSetFilter = new DistinctKeysTransactionFilter();

        Transaction tx = new Transaction(numberOfOperations);
        IntStream.range(0, numberOfOperations).forEach(index -> {
            OpType opType = opTypeGenerator.generate();

            String row = null;
            String col = null;
            boolean checked = false;

            switch (opType) {
                case READ:
                    while (!checked) {
                        row = rowKeyGenerator.generate();
                        col = columnKeyGenerator.generate();
                        checked = readSetFilter.check(new Key(row, col));
                    }

                    tx.addOp(new ReadOperation(row, col));
                    break;
                case WRITE:
                    while (!checked) {
                        row = rowKeyGenerator.generate();
                        col = columnKeyGenerator.generate();
                        checked = writeSetFilter.check(new Key(row, col));
                    }

                    tx.addOp(new WriteOperation(row, col, WithPrefixAndSequenceValueGenerator.next(row, col)));
                    break;
                default:
                    throw new IllegalArgumentException(String.format("No such OpType: %s.", opType));
            }

        });

        RVSISpecificationManager rvsiSpecManager = rvsiSpecGenerator.generateRVSISpecManager(tx.getReadOps());
        tx.setRvsiSpecManager(rvsiSpecManager);

        LOGGER.debug("Tx is [{}].", tx);
        return tx;
    }

}
