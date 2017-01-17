package benchmarking.workload.operation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class WriteOperation extends Operation {
    private static final Logger LOGGER = LoggerFactory.getLogger(WriteOperation.class);

    public WriteOperation(String row, String col, String val) {
        super(row, col, val);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(row, col, val);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(o == null)
            return false;
        if(! (o instanceof WriteOperation))
            return false;

        WriteOperation that = (WriteOperation) o;
        return Objects.equal(this.row, that.row)
                && Objects.equal(this.col, that.col)
                && Objects.equal(this.val, that.val);
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
