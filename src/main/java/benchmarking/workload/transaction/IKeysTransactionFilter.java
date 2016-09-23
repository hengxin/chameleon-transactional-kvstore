package benchmarking.workload.transaction;

import benchmarking.workload.keyspace.Key;

/**
 * @author hengxin
 * @date 16-9-15
 */
public interface IKeysTransactionFilter {
    boolean check(Key key);
}
