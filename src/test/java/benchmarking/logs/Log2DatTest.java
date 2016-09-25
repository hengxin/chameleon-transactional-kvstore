package benchmarking.logs;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * {@link Log2DatTest} tests {@link Log2Dat}.
 *
 * Created by hengxin on 16-9-25.
 */
public class Log2DatTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Log2DatTest.class);

    private static final String LOG_DIR = "src/test/resources/benchmarking/logs" + File.separator;
    private static final String LOG_FILE = LOG_DIR + "log-rwRatio-0.5.log";

    private final Log2Dat log2Table = new Log2Dat(LOG_FILE);

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void saveAsDat() throws Exception {
        log2Table.saveAsDat();
    }

}