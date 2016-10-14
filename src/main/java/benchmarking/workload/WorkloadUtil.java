package benchmarking.workload;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Properties;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * @author hengxin
 * @date 16-9-8
 */
@NotThreadSafe
public class WorkloadUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkloadUtil.class);

    public static Properties DEFAULT_WORKLOAD_PROPERTIES;
    static {
        DEFAULT_WORKLOAD_PROPERTIES = new Properties();
        Arrays.stream(WorkloadParams.values())
                .forEach(WorkloadUtil::setProperty);
    }

    public enum WorkloadParams {
        SIZE_OF_KEYSPACE("sizeOfKeyspace", "5"),
        MPL("mpl", "5"),
        NUMBER_OF_TRANSACTIONS("numberOfTransactions", "1000"),
        MAX_NUMBER_OF_OPERATIONS_PER_TRANSACTION("maxNumberOfOperations", "20"),
        PROB_BINOMIAL("probBinomial", "50"),
        RW_RATIO("rwRatio", "2"),
        ZIPF_EXPONENT("zipfExponent", "1"),
        MEAN_TIME_INTER_TRANSACTIONS("meanTimeInterTransactions", "20"),
        MIN_TIME_INTER_TRANSACTIONS("minTimeInterTransactions", "10"),
        MAX_TIME_INTER_TRANSACTIONS("maxTimeInterTransactions", "50"),
        K1BV("k1", "1"),
        K2FV("k2", "0"),
        K3SV("k3", "1"),
        RVSI("rvsi", "(1,0,1)"),
        SIMULATION("simulation", "false"),
        INTRA_DC_DELAY("intraDCDelay", "5"),
        INTER_DC_DELAY("interDCDelay", "40");

        private final String param;
        private final String val;

        WorkloadParams(String param, String val) {
            this.param = param;
            this.val = val;
        }

        public String param() { return param; }
        public String val() { return val; }

    }

    private static void setProperty(@NotNull WorkloadParams param) {
        DEFAULT_WORKLOAD_PROPERTIES.setProperty(param.param, param.val);
    }

    @Override
    public String toString() {
        return DEFAULT_WORKLOAD_PROPERTIES.toString();
    }

}
