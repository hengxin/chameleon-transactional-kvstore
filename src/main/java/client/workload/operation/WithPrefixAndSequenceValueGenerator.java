package client.workload.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * @author hengxin
 * @date 16-9-8
 */
@NotThreadSafe
public class WithPrefixAndSequenceValueGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(WithPrefixAndSequenceValueGenerator.class);

    private static Map<String, Integer> valueMap = new HashMap<>();

    public static String next(final String row, final String col) {
        String prefix = row + '-' + col;
        valueMap.computeIfAbsent(prefix, k -> 0);
        return String.valueOf(valueMap.computeIfPresent(prefix, (k, v) -> v++));
    }

}
