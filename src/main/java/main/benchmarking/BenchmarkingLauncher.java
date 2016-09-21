package main.benchmarking;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.IntStream;

import benchmarking.batch.RVSITriple;
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
import benchmarking.workload.WorkloadUtil;
import benchmarking.workload.overall.Workload;
import benchmarking.workload.overall.WorkloadGeneratorFromProperties;
import client.clientlibrary.partitioning.ConsistentHashingDynamicPartitioner;
import client.context.AbstractClientContext;
import client.context.ClientContextMultiMaster;
import util.PropertiesUtil;

import static benchmarking.workload.WorkloadUtil.WorkloadParams.K1BV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K2FV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K3SV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.MAX_TIME_INTER_TRANSACTIONS;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.MEAN_TIME_INTER_TRANSACTIONS;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.MIN_TIME_INTER_TRANSACTIONS;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.MPL;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.RVSI;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.RW_RATIO;

/**
 * @author hengxin
 * @date 16-9-11
 */
public class BenchmarkingLauncher {
    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkingLauncher.class);

    @NotNull
    private final AbstractClientContext cctx;
    @NotNull
    private final Workload workload;

    private Properties prop;

    public BenchmarkingLauncher(final String workloadProperties,
                                final String siteProperties, final String cfProperties, final String toProperties) {
        cctx = new ClientContextMultiMaster(siteProperties, cfProperties, toProperties,
                ConsistentHashingDynamicPartitioner.INSTANCE);
        workload = new WorkloadGeneratorFromProperties(workloadProperties).generate();

        try {
            prop = PropertiesUtil.load(workloadProperties);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public BenchmarkingLauncher(final Properties workloadProperties,
                                final String siteProperties, final String cfProperties, final String toProperties) {
        cctx = new ClientContextMultiMaster(siteProperties, cfProperties, toProperties,
                ConsistentHashingDynamicPartitioner.INSTANCE);
        prop = workloadProperties;
        workload = new WorkloadGeneratorFromProperties(prop).generate();
    }

    public BenchmarkingLauncher(final double rwRatio, final int mpl, final RVSITriple rvsiTriple,
                                final String siteProperties, final String cfProperties, final String toProperties) {
        prop = fillWorkloadProperties(rwRatio, mpl, rvsiTriple);

        cctx = new ClientContextMultiMaster(siteProperties, cfProperties, toProperties,
                ConsistentHashingDynamicPartitioner.INSTANCE);
        workload = new WorkloadGeneratorFromProperties(prop).generate();
    }

    public BenchmarkingLauncher(final String rwRatioStr, final String mplStr, final String rvsiStr,
                                final String siteProperties, final String cfProperties, final String toProperties) {
        this(Double.parseDouble(rwRatioStr),
                Integer.parseInt(mplStr),
                new RVSITriple(rvsiStr),
                siteProperties, cfProperties, toProperties);
    }

    public @Nullable IWorkloadStatistics run() {
        final int numberOfClients = workload.getNumberOfClients();
        List<IClientExecutor> clientExecutors = new ArrayList<>(numberOfClients);

        // FIXME: move to the upper-level class
        int meanTimeInterTransactions = Integer.parseInt(prop.getProperty(MEAN_TIME_INTER_TRANSACTIONS.param()));
        int minTimeInterTransactions = Integer.parseInt(prop.getProperty(MIN_TIME_INTER_TRANSACTIONS.param()));
        int maxTimeInterTransactions = Integer.parseInt(prop.getProperty(MAX_TIME_INTER_TRANSACTIONS.param()));
        IInterIssueTimeGenerator interIssueTimeGenerator = new ExponentialInterIssueTimeGenerator
                (minTimeInterTransactions, maxTimeInterTransactions, meanTimeInterTransactions);

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

        return workloadExecutor.getWorkloadStat();
    }


    private Properties fillWorkloadProperties(double rwRatio, int mpl, RVSITriple rvsiTriple) {
        Properties prop = WorkloadUtil.DEFAULT_WORKLOAD_PROPERTIES;

        prop.setProperty(MPL.param(), String.valueOf(mpl));
        prop.setProperty(RW_RATIO.param(), String.valueOf(rwRatio));

        prop.setProperty(RVSI.param(), rvsiTriple.rvsiParamVal());
        prop.setProperty(K1BV.param(), String.valueOf(rvsiTriple.getK1()));
        prop.setProperty(K2FV.param(), String.valueOf(rvsiTriple.getK2()));
        prop.setProperty(K3SV.param(), String .valueOf(rvsiTriple.getK3()));

        return prop;
    }


}
