package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.stream.Collectors;

import benchmarking.workload.WorkloadUtil.WorkloadParams;

/**
 * {@link LogUtil} provides utility functions for analyzing logs.
 *
 * Created by hengxin on 16-9-25.
 */
public class LogUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);
    public static final String LOG_FILE_PREFIX = "log";
    public static final String LOG_FILE_EXTENSION = ".log";
    public static final String  LOG_FILE_SEP = "-";

    /**
     * Extract (the first occurrence of) the value
     * between (the end of the) <code>start</code> String and
     * (the start of the) <code>end</code> String.
     *
     * @param line  from which the value is extracted
     * @param start the start string
     * @param end   the end string
     * @return  the value between (the end of the) <code>start</code> String
     *  and (the start of the) <code>end</code> String.
     */
    public static String extract(String line, String start, String end) {
        int startEndIndex = line.indexOf(start) + start.length();
        int endBeginIndex = line.indexOf(end, startEndIndex);

        return line.substring(startEndIndex, endBeginIndex);
    }

    /**
     * Extract the {@link WorkloadParams} value for <code>param</code> from <code>this</code> line.
     * This <code>line</code> <it>should</it> be in the form of
     * "xxx param=xx, xxx".
     *
     * @param line  from which the parameter value is extracted
     * @return  the parameter value
     */
    public static String extractParamVal(final String line, final WorkloadParams param) {
        return extract(line, param.param() + "=", ",");
    }

    /**
     * Extract a String format of rvsi: just appending k1, k2, and k3
     * @param line  a line containing rvsi
     * @return  String format of rvsi
     */
    public static String extractRvsi(final String line) {
        String rvsi = extract(line, "RVSITriple{", "}");
        return Arrays.stream(rvsi.split(","))
                .map(String::trim)
                .map(kv -> kv.split("=")[1])
                .collect(Collectors.joining(""));
    }

    public static String extractAborts(String line) {
        return extract(line, "WorkloadStatistics{", "}");
    }

    public static boolean containsData(String line) {
        return line.contains("BatchRunner");
    }

}
