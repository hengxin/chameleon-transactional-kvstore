package benchmarking.workload.transaction;

import java.util.HashSet;
import java.util.Set;

import benchmarking.workload.keyspace.Key;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class DistinctKeysTransactionFilter implements IKeysTransactionFilter {
    private final Set<Key> keys = new HashSet<>();

    public boolean check(Key key) {
        return keys.add(key);
    }

}
