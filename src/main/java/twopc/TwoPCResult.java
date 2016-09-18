package twopc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * @author hengxin
 * @date 16-9-18
 */
public final class TwoPCResult implements Serializable {
    private static final long serialVersionUID = -6969611847596869005L;
    private static final Logger LOGGER = LoggerFactory.getLogger(TwoPCResult.class);

    private final PreparedResult preparedResult;
    private final boolean isCommitted;

    public TwoPCResult(PreparedResult preparedResult, boolean isCommitted) {
        this.preparedResult = preparedResult;
        this.isCommitted = isCommitted;
    }

    public boolean isCommitted() { return isCommitted; }
    public boolean isVcChecked() { return preparedResult.isVcChecked(); }
    public boolean isWcfChecked() { return preparedResult.isWcfChecked(); }

    @Override
    public int hashCode() {
        return Objects.hashCode(preparedResult, isCommitted);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || ! (obj instanceof TwoPCResult))
            return false;

        TwoPCResult that = (TwoPCResult) obj;
        return Objects.equal(this.preparedResult, that.preparedResult)
                && Objects.equal(this.isCommitted, that.isCommitted);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(preparedResult)
                .add("isCommitted", isCommitted)
                .toString();
    }

}
