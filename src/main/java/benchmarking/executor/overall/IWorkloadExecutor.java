package benchmarking.executor.overall;

import org.jetbrains.annotations.Nullable;

import benchmarking.statistics.AbstractWorkloadStatistics;

/**
 * @author hengxin
 * @date 16-9-11
 */
public interface IWorkloadExecutor {
    void execute();
    @Nullable AbstractWorkloadStatistics getWorkloadStat();
}
