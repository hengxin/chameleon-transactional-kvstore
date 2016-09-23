package twopc;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

import client.clientlibrary.rvsi.vc.vcresult.VCCheckedResult;

/**
 * @author hengxin
 * @date 16-9-18
 */
@Immutable
public final class PreparedResult implements Serializable {
    private static final long serialVersionUID = -7995627777569816602L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PreparedResult.class);
    public static final PreparedResult IDENTITY = new PreparedResult(VCCheckedResult.IDENTITY, true);

    private final VCCheckedResult vcCheckedResult;
    private final boolean wcfChecked;

    private final boolean prepareChecked;

    public PreparedResult(final VCCheckedResult vcCheckedResult, boolean wcfChecked) {
        this.vcCheckedResult = vcCheckedResult;
        this.wcfChecked = wcfChecked;

        this.prepareChecked = this.vcCheckedResult.isVcChecked() && this.wcfChecked;
    }

    public static PreparedResult accumulate(final PreparedResult fst,
                                            final PreparedResult snd) {
        return new PreparedResult(
                VCCheckedResult.accumulate(fst.vcCheckedResult, snd.vcCheckedResult),
                fst.wcfChecked && snd.wcfChecked);
    }

    public boolean isBVChecked() { return vcCheckedResult.isBvChecked(); }
    public boolean isFVChecked() { return vcCheckedResult.isFvChecked(); }
    public boolean isSVChecked() { return vcCheckedResult.isSvChecked(); }

    public boolean isVcChecked() { return vcCheckedResult.isVcChecked(); }
    public boolean isWcfChecked() { return wcfChecked; }
    public boolean isPrepareChecked() { return prepareChecked; }

    @Override
    public int hashCode() { return Objects.hashCode(vcCheckedResult, wcfChecked); }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || ! (obj instanceof PreparedResult))
            return false;

        PreparedResult that = (PreparedResult) obj;
        return Objects.equal(this.vcCheckedResult, that.vcCheckedResult)
                && Objects.equal(this.wcfChecked, that.wcfChecked);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("vcCheckedResult", vcCheckedResult)
                .add("wcfChecked", wcfChecked)
                .toString();
    }

}
