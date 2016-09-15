package main.benchmarking;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

import benchmarking.executor.ExponentialInterIssueTimeGenerator;
import benchmarking.executor.IInterIssueTimeGenerator;
import benchmarking.executor.client.ClientExecutor;
import benchmarking.executor.client.IClientExecutor;
import benchmarking.executor.overall.IWorkloadExecutor;
import benchmarking.executor.overall.WorkloadExecutor;
import benchmarking.executor.transaction.RVSITransactionExecutor;
import benchmarking.statistics.ClientStatistics;
import benchmarking.statistics.IWorkloadStatistics;
import benchmarking.statistics.WorkloadStatistics;
import benchmarking.workload.WorkloadUtil.WorkloadParams;
import benchmarking.workload.overall.IWorkloadGenerator;
import benchmarking.workload.overall.Workload;
import benchmarking.workload.overall.WorkloadGeneratorFromProperties;
import client.clientlibrary.partitioning.ConsistentHashingDynamicPartitioner;
import client.context.AbstractClientContext;
import client.context.ClientContextMultiMaster;
import util.PropertiesUtil;

/**
 * @author hengxin
 * @date 16-9-11
 */
public class BenchmarkingLauncher {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkingLauncher.class);

    @NotNull
    private final AbstractClientContext cctx;
    @NotNull
    private final IWorkloadGenerator workloadGenerator;

    private Properties prop;

    BenchmarkingLauncher(final String workloadProperties,
                         final String siteProperties, final String cfProperties, final String toProperties) {
        cctx = new ClientContextMultiMaster(siteProperties, cfProperties, toProperties,
                ConsistentHashingDynamicPartitioner.INSTANCE);
        workloadGenerator = new WorkloadGeneratorFromProperties(workloadProperties);

        try {
            prop = PropertiesUtil.load(workloadProperties);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void run() {
        Workload workload = workloadGenerator.generate();

        final int numberOfClients = workload.getNumberOfClients();
        List<IClientExecutor> clientExecutors = new ArrayList<>(numberOfClients);

        // FIXME: move to the upper-level class
        int meanTimeInterTransactions = Integer.parseInt(prop.getProperty(WorkloadParams.MEAN_TIME_INTER_TRANSACTIONS.getParam()));
        int minTimeInterTransactions = Integer.parseInt(prop.getProperty(WorkloadParams .MIN_TIME_INTER_TRANSACTIONS.getParam()));
        IInterIssueTimeGenerator interIssueTimeGenerator = new ExponentialInterIssueTimeGenerator
                (minTimeInterTransactions, meanTimeInterTransactions);

        IntStream.range(0, numberOfClients)
                .forEach(cid -> clientExecutors.add(new ClientExecutor(
                        new RVSITransactionExecutor(cctx),
                        interIssueTimeGenerator,
                        new ClientStatistics()
                )));

        IWorkloadExecutor workloadExecutor = new WorkloadExecutor(
                workload, clientExecutors,
                new WorkloadStatistics());

        workloadExecutor.execute();

        IWorkloadStatistics workloadStat = workloadExecutor.getWorkloadStat();
        if (workloadStat != null)
            LOGGER.info(workloadStat.summaryReport());
    }

}
