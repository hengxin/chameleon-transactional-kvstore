package client.workload.operation;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class Operation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Operation.class);

    protected final String row;
    protected final String col;
    protected String val;

    Operation(String row, String col, String val) {
        this.row = row;
        this.col = col;
        this.val = val;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(row)
                .addValue(col)
                .addValue(val)
                .toString();
    }

}
