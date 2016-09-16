package benchmarking.executor.overall;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.IntStream;

import benchmarking.executor.client.IClientExecutor;
import benchmarking.statistics.IWorkloadStatistics;
import benchmarking.workload.client.ClientWorkload;
import benchmarking.workload.overall.Workload;

/**
 * @author hengxin
 * @date 16-9-11
 */
public class WorkloadExecutor implements IWorkloadExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadExecutor.class);

    private final Workload workload;
    private final List<IClientExecutor> clientExecutors;    // TODO: refactor to use {@link Iterable}
    @Nullable
    private final IWorkloadStatistics workloadStat;

    /**
     * @param workload  {@link Workload} to be executed
     * @param clientExecutors   a list of {@link IClientExecutor}s
*                          which are to execute {@code workload}
     * @param workloadStat  collect data during executing and do statistic analysis afterwards
     */
    public WorkloadExecutor(final Workload workload,
                            final List<IClientExecutor> clientExecutors,
                            @Nullable final IWorkloadStatistics workloadStat) {
        this.workload = workload;
        this.clientExecutors = clientExecutors;
        this.workloadStat = workloadStat;
    }

    /**
     * Dispatch {@link #workload} to {@link #clientExecutors}
     * for parallel executions.
     */
    @Override
    public void execute() {
        List<ClientWorkload> clientWorkloads = workload.getClientWorkloads();

        IntStream.range(0, workload.getNumberOfClients())
                .parallel()
                .forEach(cid -> {
                    IClientExecutor clientExecutor = clientExecutors.get(cid);
                    clientExecutor.execute(clientWorkloads.get(cid));

                    if (workloadStat != null)
                        workloadStat.collect(clientExecutor.getClientStat());
                });
    }

    @Override
    public @Nullable IWorkloadStatistics getWorkloadStat() { return workloadStat; }
}
