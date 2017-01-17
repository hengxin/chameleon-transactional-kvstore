package client.clientlibrary.rvsi.vc.vcresult;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

import javax.annotation.concurrent.Immutable;

import client.clientlibrary.rvsi.vc.BVVersionConstraint;
import client.clientlibrary.rvsi.vc.FVVersionConstraint;
import client.clientlibrary.rvsi.vc.SVVersionConstraint;

/**
 * {@link VCCheckedResult} encapsulates the results of
 * checking {@link BVVersionConstraint}, {@link FVVersionConstraint},
 * and {@link SVVersionConstraint}.
 *
 * Created by hengxin on 16-9-22.
 */
@Immutable
public class VCCheckedResult implements Serializable {
    private static final long serialVersionUID = 6300505517965772643L;
    public static final VCCheckedResult IDENTITY = new VCCheckedResult(true, true, true);

    private final boolean bvChecked;
    private final boolean fvChecked;
    private final boolean svChecked;

    private final boolean vcChecked;

    public VCCheckedResult(boolean bvChecked, boolean fvChecked, boolean svChecked) {
        this.bvChecked = bvChecked;
        this.fvChecked = fvChecked;
        this.svChecked = svChecked;

        this.vcChecked = this.bvChecked && this.fvChecked && this.svChecked;
    }

    /**
     * Accumulate two {@link VCCheckedResult}s into one
     * by "AND-ing" their separate components.
     *
     * @param fst the first {@link VCCheckedResult}
     * @param snd the second {@link VCCheckedResult}
     * @return a new accumulated {@link VCCheckedResult}
     */
    @Contract("_, _ -> !null")
    @NotNull
    public static VCCheckedResult accumulate(final VCCheckedResult fst, final VCCheckedResult snd) {
        return new VCCheckedResult(fst.bvChecked && snd.bvChecked,
                fst.fvChecked && snd.fvChecked,
                fst.svChecked && snd.svChecked);
    }

    public boolean isBvChecked() { return bvChecked; }
    public boolean isFvChecked() { return fvChecked; }
    public boolean isSvChecked() { return svChecked; }

    public boolean isVcChecked() { return vcChecked; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VCCheckedResult that = (VCCheckedResult) o;
        return bvChecked == that.bvChecked &&
                fvChecked == that.fvChecked &&
                svChecked == that.svChecked;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bvChecked, fvChecked, svChecked);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("bvChecked", bvChecked)
                .add("fvChecked", fvChecked)
                .add("svChecked", svChecked)
                .toString();
    }

}
