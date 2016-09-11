package benchmarking.executor.overall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.IntStream;

import benchmarking.executor.client.IClientExecutor;
import benchmarking.workload.client.ClientWorkload;
import benchmarking.workload.overall.Workload;
import client.context.AbstractClientContext;

/**
 * @author hengxin
 * @date 16-9-11
 */
public class WorkloadExecutor implements IWorkloadExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadExecutor.class);

    private final AbstractClientContext cctx;
    private final Workload workload;
    private final List<IClientExecutor> clientExecutors;

    /**
     * @param cctx  {@link AbstractClientContext} in which
     *     this {@code workload} is to be executed
     * @param workload  {@link Workload} to be executed
     * @param clientExecutors   a list of {@link IClientExecutor}s
 *                          which are to execute {@code workload}
     */
    public WorkloadExecutor(final AbstractClientContext cctx,
                            final Workload workload,
                            final List<IClientExecutor> clientExecutors) {
        this.cctx = cctx;
        this.workload = workload;
        this.clientExecutors = clientExecutors;
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
                .forEach(cid ->
                        clientExecutors.get(cid).execute(clientWorkloads.get(cid)));
    }

}
