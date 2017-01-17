package benchmarking.statistics;

import twopc.TransactionCommitResult;

/**
 * @author hengxin
 * @date 16-9-15
 */
public abstract class AbstractClientStatistics extends AbstractStatistics {
    public abstract void collect(TransactionCommitResult transactionCommitResult);
}
