package benchmarking.statistics;

/**
 * @author hengxin
 * @date 16-9-15
 */
public interface IClientStatistics extends IStatistics {
    void incCommitted();
    void incAborted();
    void incFalseVcChecked();
    void incFalseWcfChecked();

    int countCommitted();
    int countAborted();
    int countFalseVcChecked();
    int countFalseWcfChecked();

    int countAll();
}
