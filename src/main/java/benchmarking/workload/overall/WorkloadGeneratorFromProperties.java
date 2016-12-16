package benchmarking.workload.overall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import benchmarking.workload.WorkloadUtil;
import benchmarking.workload.client.ClientWorkloadGenerator;
import benchmarking.workload.operation.ZipfKeyGenerator;
import benchmarking.workload.rvsi.UniformRVSISpecificationGenerator;
import benchmarking.workload.transaction.BinomialTransactionSizeGenerator;
import benchmarking.workload.transaction.TransactionGenerator;
import utils.PropertiesUtil;

import static benchmarking.workload.WorkloadUtil.WorkloadParams.K1BV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K2FV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.K3SV;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.MAX_NUMBER_OF_OPERATIONS_PER_TRANSACTION;
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

    /**
     *  Constructor which uses {@link WorkloadUtil#DEFAULT_WORKLOAD_PROPERTIES}
     */
    public WorkloadGeneratorFromProperties() { }

    public WorkloadGeneratorFromProperties(Properties prop) {
        WorkloadUtil.setWorkloadParams(prop);
        LOGGER.info("WorkloadParams are [{}].", prop.toString());
    }

    public WorkloadGeneratorFromProperties(final String workloadProperties) {
        try {
            Properties prop = PropertiesUtil.load(workloadProperties);
            WorkloadUtil.setWorkloadParams(prop);
            LOGGER.info("WorkloadParams are [{}].", prop.toString());
        } catch (IOException ioe) {
            LOGGER.error("Failed to load workload parameters from workload properties [{}] due to [{}].",
                    workloadProperties, ioe);
            LOGGER.info("WorkloadParams are [{}].", WorkloadUtil.DEFAULT_WORKLOAD_PROPERTIES.toString());
        }
    }

    @Override
    public Workload generate() {
        int sizeOfKeyspace = Integer.parseInt(SIZE_OF_KEYSPACE.val());
        int mpl = Integer.parseInt(MPL.val());

        int numberOfTransactions = Integer.parseInt(NUMBER_OF_TRANSACTIONS.val());

        int maxNumberOfOperationsPerTransaction = Integer.parseInt(MAX_NUMBER_OF_OPERATIONS_PER_TRANSACTION.val());
        int probBinomial = Integer.parseInt(PROB_BINOMIAL.val());

        double rwRatio = Double.parseDouble(RW_RATIO.val());
        int zipfExponent = Integer.parseInt(ZIPF_EXPONENT.val());

        int k1 = Integer.parseInt(K1BV.val());
        int k2 = Integer.parseInt(K2FV.val());
        int k3 = Integer.parseInt(K3SV.val());

        IWorkloadGenerator workloadGenerator =
                new WorkloadGenerator(mpl,
                        new ClientWorkloadGenerator(numberOfTransactions,
                                new TransactionGenerator(new BinomialTransactionSizeGenerator(
                                            maxNumberOfOperationsPerTransaction, probBinomial),
                                        rwRatio,
                                        new ZipfKeyGenerator(sizeOfKeyspace, zipfExponent),
                                        new ZipfKeyGenerator(sizeOfKeyspace, zipfExponent),
                                        new UniformRVSISpecificationGenerator(k1, k2, k3))));

        return workloadGenerator.generate();
    }

}
