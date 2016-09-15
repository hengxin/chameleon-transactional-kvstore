package benchmarking.workload.operation;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hengxin
 * @date 16-9-7
 */
public class ReadOperation extends Operation {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadOperation.class);

    public ReadOperation(String row, String col) {
        super(row, col, null);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(row, col);
    }

    @Override
    public boolean equals(Object o) {
        if(o == this)
            return true;
        if(o == null)
            return false;
        if(! (o instanceof ReadOperation))
            return false;

        ReadOperation that = (ReadOperation) o;
        return Objects.equal(this.row, that.row)
                && Objects.equal(this.col, that.col);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(row)
                .addValue(col)
                .toString();
    }

}
