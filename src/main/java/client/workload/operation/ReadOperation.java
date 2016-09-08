package client.workload.operation;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class ReadOperation extends Operation {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadOperation.class);

    ReadOperation(String row, String col) {
        super(row, col, null);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(row)
                .addValue(col)
                .toString();
    }
}
