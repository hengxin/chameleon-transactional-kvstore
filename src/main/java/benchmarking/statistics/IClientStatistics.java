package benchmarking.statistics;

/**
 * @author hengxin
 * @date 16-9-15
 */
public interface IClientStatistics {
    void incCommitted();
    void incAborted();

    int countCommitted();
    int countAborted();

    int countAll();

    String summaryReport();
}