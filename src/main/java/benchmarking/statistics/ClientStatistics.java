package benchmarking.statistics;

import com.google.common.base.MoreObjects;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class ClientStatistics implements IClientStatistics {
    private int numberOfCommittedTransactions = 0;
    private int numberOfAbortedTransactions = 0;
    private int numberOfTransactions = 0;

    public void incCommitted() {
        numberOfCommittedTransactions++;
        numberOfTransactions++;
    }

    public void incAborted() {
        numberOfAbortedTransactions++;
        numberOfTransactions++;
    }

    @Override
    public int countCommitted() { return numberOfCommittedTransactions; }

    @Override
    public int countAborted() { return numberOfAbortedTransactions; }

    @Override
    public int countAll() { return numberOfTransactions; }

    @Override
    public String summaryReport() {
        return MoreObjects.toStringHelper(this)
                .add("#C", numberOfCommittedTransactions)
                .add("#A", numberOfAbortedTransactions)
                .add("#T", numberOfTransactions)
                .add("#C/#T", (numberOfCommittedTransactions * 1.0) / numberOfTransactions)
                .add("#A/#T", (numberOfAbortedTransactions * 1.0) / numberOfTransactions)
                .toString();
    }

}
