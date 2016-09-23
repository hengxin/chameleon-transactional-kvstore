package benchmarking.statistics;

import org.jetbrains.annotations.Nullable;

/**
 * @author hengxin
 * @date 16-9-15
 */
public abstract class AbstractWorkloadStatistics extends AbstractStatistics {
    public abstract void collect(final @Nullable AbstractClientStatistics clientStat);
}
