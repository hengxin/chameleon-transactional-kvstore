package twopc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

/**
 * @author hengxin
 * @date 16-9-18
 */
@Immutable
public final class PreparedResult implements Serializable {
    private static final long serialVersionUID = -7995627777569816602L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PreparedResult.class);
    public static final PreparedResult IDENTITY = new PreparedResult(true, true);

    private final boolean vcChecked;
    private final boolean wcfChecked;

    private final boolean checked;

    public PreparedResult(boolean vcChecked, boolean wcfChecked) {
        this.vcChecked = vcChecked;
        this.wcfChecked = wcfChecked;

        this.checked = this.vcChecked && this.wcfChecked;
    }

    public static PreparedResult accumulate(final PreparedResult firstPreparedResult,
                                            final PreparedResult secondPreparedResult) {
        return new PreparedResult(firstPreparedResult.vcChecked && secondPreparedResult.vcChecked,
                firstPreparedResult.wcfChecked && secondPreparedResult.wcfChecked);
    }

    public boolean isVcChecked() { return vcChecked; }
    public boolean isWcfChecked() { return wcfChecked; }
    public boolean isChecked() { return checked; }

    @Override
    public int hashCode() {
        return Objects.hashCode(vcChecked, wcfChecked);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || ! (obj instanceof PreparedResult))
            return false;

        PreparedResult that = (PreparedResult) obj;
        return Objects.equal(this.vcChecked, that.vcChecked)
                && Objects.equal(this.wcfChecked, that.wcfChecked);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("vcChecked", vcChecked)
                .add("wcfChecked", wcfChecked)
                .toString();
    }
}
