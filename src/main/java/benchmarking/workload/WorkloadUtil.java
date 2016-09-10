package benchmarking.workload;

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
        SIZE_OF_KEYSPACE("sizeOfKeyspace", "1000"),
        MPL("mpl", "5"),
        NUMBER_OF_TRANSACTIONS("numberOfTransactions", "1000"),
        MAX_NUMBER_OF_OPERATIONS_PER_TRANSACTION("maxNumberOfOperations", "4"),
        PROB_BINOMIAL("probBinomial", "0.50"),
        RW_RATIO("rwRatio", "4"),
        ZIPF_EXPONENT("zipfExponent", "1"),
        MEAN_TIME_INTER_TRANSACTIONS("meanTimeInterTransactions", "500"),
        K1BV("k1", "1"),
        K2FV("k2", "0"),
        K3SV("k3", "2");

        private final String param;
        private final String val;

        WorkloadParams(String param, String val) {
            this.param = param;
            this.val = val;
        }

        public String getParam() { return param; }
    }

    private static void setProperty(WorkloadParams param) {
        DEFAULT_WORKLOAD_PROPERTIES.setProperty(param.param, param.val);
    }

}
