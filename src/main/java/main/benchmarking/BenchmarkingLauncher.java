package main.benchmarking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import benchmarking.executor.client.ClientExecutor;
import benchmarking.executor.client.IClientExecutor;
import benchmarking.executor.overall.IWorkloadExecutor;
import benchmarking.executor.overall.WorkloadExecutor;
import benchmarking.executor.transaction.RVSITransactionExecutor;
import benchmarking.workload.overall.IWorkloadGenerator;
import benchmarking.workload.overall.Workload;
import benchmarking.workload.overall.WorkloadGeneratorFromProperties;
import client.clientlibrary.partitioning.ConsistentHashingDynamicPartitioner;
import client.context.AbstractClientContext;
import client.context.ClientContextMultiMaster;

/**
 * @author hengxin
 * @date 16-9-11
 */
public class BenchmarkingLauncher {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkingLauncher.class);

    private final AbstractClientContext cctx;
    private final IWorkloadGenerator workloadGenerator;

    BenchmarkingLauncher(final String workloadProperties,
                         final String siteProperties, final String cfProperties, final String toProperties) {
        cctx = new ClientContextMultiMaster(siteProperties, cfProperties, toProperties,
                ConsistentHashingDynamicPartitioner.INSTANCE);
        workloadGenerator = new WorkloadGeneratorFromProperties("client/workload.properties");
    }

    public void run() {
        Workload workload = workloadGenerator.generate();

        final int numberOfClients = workload.getNumberOfClients();
        List<IClientExecutor> clientExecutors = new ArrayList<>(numberOfClients);
        IntStream.range(0, numberOfClients)
                .forEach(cid -> clientExecutors.add(new ClientExecutor(
                        cctx, new RVSITransactionExecutor(cctx)
                )));

        IWorkloadExecutor workloadExecutor = new WorkloadExecutor(cctx, workload, clientExecutors);
        workloadExecutor.execute();
    }

}
