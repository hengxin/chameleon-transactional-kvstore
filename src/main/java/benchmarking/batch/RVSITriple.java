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

    public RVSITriple(String rvsiStr) {
        String[] rvsiParts = rvsiStr.split("-");
        this.k1 = Integer.parseInt(rvsiParts[0]);
        this.k2 = Integer.parseInt(rvsiParts[1]);
        this.k3 = Integer.parseInt(rvsiParts[2]);
    }

    public int getK1() { return k1; }
    public int getK2() { return k2; }
    public int getK3() { return k3; }

    /**
     * @return "(k1, k2, k3)"
     */
    public String rvsiParamVal() {
        StringBuilder sb = new StringBuilder();
        sb.append('(')
                .append(k1).append(',')
                .append(k2).append(',')
                .append(k3)
                .append(')');
        return sb.toString();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("k1", k1)
                .add("k2", k2)
                .add("k3", k3)
                .toString();
    }

    public static void main(String[] args) {
        new RVSITriple("1-0-0");
    }
}
