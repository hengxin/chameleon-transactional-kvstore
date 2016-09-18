package benchmarking.executor.transaction;

import benchmarking.workload.transaction.Transaction;
import twopc.TwoPCResult;

/**
 * @author hengxin
 * @date 16-9-11
 */
public interface ITransactionExecutor {
    TwoPCResult execute(Transaction tx);
}
