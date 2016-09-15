package benchmarking.executor.client;

import org.jetbrains.annotations.Nullable;

import benchmarking.statistics.IClientStatistics;
import benchmarking.workload.client.ClientWorkload;

/**
 * @author hengxin
 * @date 16-9-11
 */
public interface IClientExecutor {
    void execute(ClientWorkload clientWorkload);
    @Nullable IClientStatistics getClientStat();
}
