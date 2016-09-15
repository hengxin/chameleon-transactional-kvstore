package benchmarking.executor.transaction;

import benchmarking.workload.transaction.Transaction;

/**
 * @author hengxin
 * @date 16-9-11
 */
public interface ITransactionExecutor {
    boolean execute(Transaction tx);
}
