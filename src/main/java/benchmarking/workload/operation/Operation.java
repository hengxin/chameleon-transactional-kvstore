package benchmarking.workload.operation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.CompoundKey;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class Operation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Operation.class);

    protected final String row;
    protected final String col;
    String val;

    private final CompoundKey ck;

    Operation(String row, String col, String val) {
        this.row = row;
        this.col = col;
        this.val = val;

        this.ck = new CompoundKey(new Row(row), new Column(col));
    }

    public CompoundKey getCK() {
        return ck;
    }

}
