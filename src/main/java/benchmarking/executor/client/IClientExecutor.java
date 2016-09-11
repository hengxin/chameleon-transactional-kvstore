package benchmarking.executor.client;

import benchmarking.workload.client.ClientWorkload;

/**
 * @author hengxin
 * @date 16-9-11
 */
public interface IClientExecutor {
    void execute(ClientWorkload clientWorkload);
}
