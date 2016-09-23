package benchmarking.statistics;

import com.google.common.base.MoreObjects;

import benchmarking.batch.RVSITriple;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class BenchmarkingStatistics implements IStatistics {
    private final double rwRatio;
    private final int mpl;
    private final RVSITriple rvsiTriple;

    private final AbstractWorkloadStatistics workloadStat;

    public BenchmarkingStatistics(double rwRatio, int mpl, final RVSITriple rvsiTriple,
                                  AbstractWorkloadStatistics workloadStat) {
        this.rwRatio = rwRatio;
        this.mpl = mpl;
        this.rvsiTriple = rvsiTriple;
        this.workloadStat = workloadStat;
    }

    @Override
    public String briefReport() {
        return MoreObjects.toStringHelper(this)
                .add("rwRatio", rwRatio)
                .add("mpl", mpl)
                .add("rvsiTriple", rvsiTriple)
                .add("workloadStat", workloadStat.briefReport())
                .toString();
    }

    @Override
    public String summaryReport() {
        return MoreObjects.toStringHelper(this)
                .add("rwRatio", rwRatio)
                .add("mpl", mpl)
                .add("rvsiTriple", rvsiTriple)
                .add("workloadStat", workloadStat.summaryReport())
                .toString();
    }

}
