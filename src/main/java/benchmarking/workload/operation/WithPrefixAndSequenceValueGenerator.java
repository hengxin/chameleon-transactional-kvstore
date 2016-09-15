package benchmarking.workload.operation;

import org.jetbrains.annotations.NotNull;
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

    @NotNull
    private static Map<String, Integer> valueMap = new HashMap<>();

    public static String next(final String row, final String col) {
        String prefix = row + '-' + col;

        if (valueMap.get(prefix) != null)
            return String.valueOf(valueMap.computeIfPresent(prefix, (k, v) -> v + 1));
        else return String.valueOf(valueMap.compute(prefix, (k, v) -> 0));
    }

}
