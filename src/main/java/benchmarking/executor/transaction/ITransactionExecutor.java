package benchmarking.executor.transaction;

import benchmarking.workload.transaction.Transaction;
import twopc.TransactionCommitResult;

/**
 * @author hengxin
 * @date 16-9-11
 */
public interface ITransactionExecutor {
    TransactionCommitResult execute(Transaction tx);
}
