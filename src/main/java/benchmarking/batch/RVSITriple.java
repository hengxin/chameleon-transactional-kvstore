package benchmarking.batch;

import com.google.common.base.MoreObjects;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class RVSITriple {
    private final int k1, k2, k3;

    public RVSITriple(int k1, int k2, int k3) {
        this.k1 = k1;
        this.k2 = k2;
        this.k3 = k3;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("k1", k1)
                .add("k2", k2)
                .add("k3", k3)
                .toString();
    }

}
