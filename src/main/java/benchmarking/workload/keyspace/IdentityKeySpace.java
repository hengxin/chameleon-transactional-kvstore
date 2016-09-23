package benchmarking.workload.keyspace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link IdentityKeySpace} <it>trivially</it> provides keys in [0, size).
 *
 * @author hengxin
 * @date 16-9-7
 */
public class IdentityKeySpace implements IKeySpace {
    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityKeySpace.class);

    private final int size;

    public IdentityKeySpace(int size) {
        this.size = size;
    }

    @Override
    public String getKey(int id) {
        if (id >= size)
            return "NULLKEY";
        return String.valueOf(id);
    }

}
