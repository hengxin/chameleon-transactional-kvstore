package benchmarking.statistics;

import org.jetbrains.annotations.Nullable;

/**
 * @author hengxin
 * @date 16-9-15
 */
public interface IWorkloadStatistics extends IStatistics {
    void collect(@Nullable final IClientStatistics clientStat);
    int countCommitted();
    int countAborted();

    int countAll();
}
