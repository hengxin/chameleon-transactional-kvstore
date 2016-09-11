package benchmarking.workload.overall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import benchmarking.workload.WorkloadUtil;
import benchmarking.workload.client.ClientWorkloadGenerator;
import benchmarking.workload.keyspace.IdentityKeySpace;
import benchmarking.workload.operation.OperationGenerator;
import benchmarking.workload.operation.StatisticalRWRatioOperationTypeGenerator;
import benchmarking.workload.operation.ZipfKeyGenerator;
import benchmarking.workload.rvsi.UniformRVSISpecificationGenerator;
import benchmarking.workload.transaction.BinomialTransactionSizeGenerator;
import benchmarking.workload.transaction.TransactionGenerator;
import util.PropertiesUtil;

import static benchmarking.workload.WorkloadUtil.WorkloadParams.K1BV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K2FV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K3SV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.MAX_NUMBER_OF_OPERATIONS_PER_TRANSACTION;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.MEAN_TIME_INTER_TRANSACTIONS;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.MPL;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.NUMBER_OF_TRANSACTIONS;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.PROB_BINOMIAL;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.RW_RATIO;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.SIZE_OF_KEYSPACE;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.ZIPF_EXPONENT;

/**
 * @author hengxin
 * @date 16-9-8
 */
public class WorkloadGeneratorFromProperties implements IWorkloadGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadGeneratorFromProperties.class);

    private Properties prop;

    /**
     *  Constructor which uses {@link WorkloadUtil#DEFAULT_WORKLOAD_PROPERTIES}
     */
    public WorkloadGeneratorFromProperties() {
        prop = WorkloadUtil.DEFAULT_WORKLOAD_PROPERTIES;
    }

    public WorkloadGeneratorFromProperties(final String workloadProperties) {
        try {
            prop = PropertiesUtil.load(workloadProperties);
        } catch (IOException ioe) {
            LOGGER.error("Failed to load workload parameters from workload properties [{}] due to [{}].",
                    workloadProperties, ioe);
            prop = WorkloadUtil.DEFAULT_WORKLOAD_PROPERTIES;
        }
    }

    @Override
    public Workload generate() {
        int sizeOfKeyspace = Integer.parseInt(prop.getProperty(SIZE_OF_KEYSPACE.getParam()));
        int mpl = Integer.parseInt(prop.getProperty(MPL.getParam()));

        int numberOfTransactions = Integer.parseInt(prop.getProperty(NUMBER_OF_TRANSACTIONS.getParam()));

        int maxNumberOfOperationsPerTransaction = Integer.parseInt(
                prop.getProperty(MAX_NUMBER_OF_OPERATIONS_PER_TRANSACTION.getParam()));
        int probBinomial = Integer.parseInt(prop.getProperty(PROB_BINOMIAL.getParam()));

        int rwRatio = Integer.parseInt(prop.getProperty(RW_RATIO.getParam()));
        int zipfExponent = Integer.parseInt(prop.getProperty(ZIPF_EXPONENT.getParam()));

        int k1 = Integer.parseInt(prop.getProperty(K1BV.getParam()));
        int k2 = Integer.parseInt(prop.getProperty(K2FV.getParam()));
        int k3 = Integer.parseInt(prop.getProperty(K3SV.getParam()));

        int meanTimeInterTransactions = Integer.parseInt(prop.getProperty(MEAN_TIME_INTER_TRANSACTIONS.getParam()));

        IWorkloadGenerator workloadGenerator = new WorkloadGenerator(mpl,
                new ClientWorkloadGenerator(numberOfTransactions,
                        new TransactionGenerator(new BinomialTransactionSizeGenerator
                                (maxNumberOfOperationsPerTransaction, probBinomial),
                                new OperationGenerator(new IdentityKeySpace(sizeOfKeyspace),
                                        new StatisticalRWRatioOperationTypeGenerator(rwRatio),
                                        new ZipfKeyGenerator(sizeOfKeyspace, zipfExponent),
                                        new ZipfKeyGenerator(sizeOfKeyspace, zipfExponent)),
                                new UniformRVSISpecificationGenerator(k1, k2, k3))));

        return workloadGenerator.generate();
    }

}
