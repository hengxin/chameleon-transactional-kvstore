package benchmarking.executor.client;

import org.jetbrains.annotations.Nullable;

import benchmarking.statistics.AbstractClientStatistics;
import benchmarking.workload.client.ClientWorkload;

/**
 * @author hengxin
 * @date 16-9-11
 */
public interface IClientExecutor {
    void execute(ClientWorkload clientWorkload);
    @Nullable AbstractClientStatistics getClientStat();
}
