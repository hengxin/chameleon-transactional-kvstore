package benchmarking.workload.keyspace;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class Key {
    private final String row;
    private final String col;

    public Key(String row, String col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(row, col);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || ! (obj instanceof Key))
            return false;

        Key that = (Key) obj;
        return Objects.equal(this.row, that.row)
                && Objects.equal(this.col, that.col);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("row", row)
                .add("col", col)
                .toString();
    }

}
