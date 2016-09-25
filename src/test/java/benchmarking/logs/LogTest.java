package benchmarking.logs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import benchmarking.workload.WorkloadUtil;

/**
 * {@link LogTest} test {@link Log}.
 *
 * Created by hengxin on 16-9-25.
 */
public class LogTest {
    private static final String LOG_DIR = "src/test/resources/benchmarking/logs" + File.separator;
    private final Log log = new Log(LOG_DIR + "log-test.log");

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testSplit() throws Exception {
        Set<String> logs = log.split(WorkloadUtil.WorkloadParams.RW_RATIO);
        Set<String> expectedLogs = Stream.of("log-rwRatio-0.5.log",
                "log-rwRatio-1.0.log", "log-rwRatio-4.0.log", "log-rwRatio-9.0.log")
                .map(LOG_DIR::concat)
                .collect(Collectors.toSet());

        Assert.assertEquals("The log contains rwRatios of 0.4, 1, 4, and 9", expectedLogs, logs);
    }

}