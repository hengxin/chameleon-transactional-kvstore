package benchmarking.workload.transaction;

import org.jetbrains.annotations.NotNull;

/**
 * @author hengxin
 * @date 16-9-7
 */
public interface ITransactionGenerator {
    @NotNull Transaction generate();
}
