package benchmarking.logs;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;

/**
 * {@link RVSIColumn} represents the column when
 * the log is stored in table.
 *
 * Created by hengxin on 16-9-26.
 */
public class RVSIColumn implements Comparable<RVSIColumn> {
    private final String rvsi;
    private final String type;

    public RVSIColumn(String rvsi, String type) {
        this.rvsi = rvsi;
        this.type = type;
    }

    public static final Comparator<RVSIColumn> RVSI_COLUMN_COMPARATOR = new Comparator<RVSIColumn>() {
        @Override
        public int compare(RVSIColumn o1, RVSIColumn o2) { return o1.compareTo(o2); }
    };

    @Override
    public int compareTo(@NotNull RVSIColumn o) {
        if (rvsi.compareTo(o.rvsi) > 0)
            return 1;
        else if (rvsi.compareTo(o.rvsi) < 0)
            return -1;
        else return type.compareTo(o.type);
    }

    /**
     * @return  String format of this rvsi column: {@link #type}{@link #rvsi}
     */
    public String getRVSIColumnTitle() {
        return type + rvsi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RVSIColumn that = (RVSIColumn) o;
        return Objects.equals(rvsi, that.rvsi) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() { return Objects.hash(rvsi, type); }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("rvsi", rvsi)
                .add("type", type)
                .toString();
    }

}
