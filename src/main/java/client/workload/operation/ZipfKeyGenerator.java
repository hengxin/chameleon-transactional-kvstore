package client.workload.operation;

import org.apache.commons.math3.distribution.ZipfDistribution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link ZipfKeyGenerator} generates (String) keys
 * according to the <a ref="https://en.wikipedia.org/wiki/Zipf%27s_law">Zipfian Distribution (wiki)</a>
 *
 * @author hengxin
 * @date 16-9-8
 */
public class ZipfKeyGenerator implements IKeyGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZipfKeyGenerator.class);

    private final ZipfDistribution zipfDist;

    public ZipfKeyGenerator(int sizeOfKeyspace, int exponent) {
        zipfDist = new ZipfDistribution(sizeOfKeyspace, exponent);
    }

    @Override
    public String generate() {
        return String.valueOf(zipfDist.sample() - 1);   // TODO: -1 ?
    }

}
