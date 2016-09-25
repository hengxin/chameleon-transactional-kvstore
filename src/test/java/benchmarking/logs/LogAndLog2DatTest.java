package benchmarking.logs;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Set;

import benchmarking.workload.WorkloadUtil;

/**
 * {@link LogAndLog2DatTest} test both {@link Log} and {@link Log2Dat}.
 *
 * Created by hengxin on 16-9-25.
 *
 * @see {@link LogTest}
 * @see {@link Log2DatTest}
 */
public class LogAndLog2DatTest {
    private static final String LOG_DIR = "src/test/resources/benchmarking/logs" + File.separator;
    private final Log log = new Log(LOG_DIR + "log-test.log");

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void test() throws Exception {
        Set<String> logs = log.split(WorkloadUtil.WorkloadParams.RW_RATIO);
        logs.forEach(log -> new Log2Dat(log).saveAsDat());
    }

}
