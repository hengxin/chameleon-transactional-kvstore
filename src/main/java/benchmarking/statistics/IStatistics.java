package benchmarking.statistics;

/**
 * {@link IStatistics} only supports brief/summary reports.
 *
 * Created by hengxin on 16-9-23.
 */
public interface IStatistics {
    String briefReport();
    String summaryReport();
}
