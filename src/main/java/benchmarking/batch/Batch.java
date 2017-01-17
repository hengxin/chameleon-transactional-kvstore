package benchmarking.batch;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import benchmarking.workload.overall.Workload;
import utils.PropertiesUtil;

import static benchmarking.workload.WorkloadUtil.WorkloadParams.MPL;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.RVSI;
import static benchmarking.workload.WorkloadUtil.WorkloadParams.RW_RATIO;
import static java.util.Arrays.stream;

/**
 * {@link Batch} represents a batch of {@link Workload}s.
 *
 * @author hengxin
 * @date 16-9-15
 */
public class Batch {
    private static final Logger LOGGER = LoggerFactory.getLogger(Batch.class);

    private Properties prop;

    private int[] mpls;
    private double[] rwRatios;
    private RVSITriple[] rvsiTriples;

    public Batch(String batchProperties) {
        try {
            prop = PropertiesUtil.load(batchProperties);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        parseMPL();
        parseRWRatio();
        parseRVSI();
    }

    int[] getMpls() { return mpls; }
    double[] getRwRatios() { return rwRatios; }
    RVSITriple[] getRvsiTriples() { return rvsiTriples; }

    private void parseMPL() {
        mpls = stream(prop.getProperty(MPL.param())
                        .split(";"))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private void parseRWRatio() {
        rwRatios = Arrays.stream(prop.getProperty(RW_RATIO.param())
                        .split(";"))
                .map(String::trim)
                .mapToDouble(Double::parseDouble)
                .toArray();
    }

    private void parseRVSI() {
        rvsiTriples = Arrays.stream(prop.getProperty(RVSI.param()).split(";"))
                .map(String::trim)
                .map(this::parseRVSITriple)
                .toArray(RVSITriple[]::new);
    }

    private RVSITriple parseRVSITriple(final String rvsiStr) {
        String[] ks = rvsiStr.split(",");
        return new RVSITriple(Integer.parseInt(ks[0]),
                Integer.parseInt(ks[1]),
                Integer.parseInt(ks[2]));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mpls", mpls)
                .add("rwRatios", rwRatios)
                .add("rvsiTriples", rvsiTriples)
                .toString();
    }

    public static void main(String[] args) {
        Batch batch = new Batch("benchmarking/batch/batch.properties");
        LOGGER.info("rwRatios: [{}].", batch.getRwRatios());
        LOGGER.info(new Batch("benchmarking/batch/batch.properties").toString());
    }
}
