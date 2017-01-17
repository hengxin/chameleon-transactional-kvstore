package benchmarking.statistics;

import com.google.common.base.MoreObjects;

/**
 * @author hengxin
 * @date 16-9-15
 */
public abstract class AbstractStatistics implements IStatistics {
    // brief overview
    protected int numberOfTransactions = 0;
    protected int numberOfCommittedTransactions = 0;
    protected int numberOfAbortedTransactions = 0;

    // details
    protected int numberOfFalseVcChecked = 0;
    protected int numberOfFalseWcfChecked = 0;

    // more details
    protected int numberOfFalseBVChecked = 0;
    protected int numberOfFalseFVChecked = 0;
    protected int numberOfFalseSVChecked = 0;

    public int countAll() { return numberOfTransactions; }
    public int countCommitted() { return numberOfCommittedTransactions; }
    public int countAborted() { return numberOfAbortedTransactions; }

    public int countFalseVcChecked() { return numberOfFalseVcChecked; }
    public int countFalseWcfChecked() { return numberOfFalseWcfChecked; }

    public int countFalseBVChecked() { return numberOfFalseBVChecked; }
    public int countFalseFVChecked() { return numberOfFalseFVChecked; }
    public int countFalseSVChecked() { return numberOfFalseSVChecked; }

    @Override
    public String briefReport() {
        return summaryReport();
    }

    @Override
    public String summaryReport() {
        return MoreObjects.toStringHelper(this)
                .add("#T", numberOfTransactions)
                .add("#C", numberOfCommittedTransactions)
                .add("#A", numberOfAbortedTransactions)
                .add("#!VC", numberOfFalseVcChecked)
                .add("#!WCF", numberOfFalseWcfChecked)
                .add("#!BV", numberOfFalseBVChecked)
                .add("#!FV", numberOfFalseFVChecked)
                .add("#!SV", numberOfFalseSVChecked)
                .add("#C/#T", (numberOfCommittedTransactions * 1.0) / numberOfTransactions)
                .add("#A/#T", (numberOfAbortedTransactions * 1.0) / numberOfTransactions)
                .add("#!VC/#T", (numberOfFalseVcChecked * 1.0) / numberOfTransactions)
                .add("#!WCF/#T", (numberOfFalseWcfChecked * 1.0) / numberOfTransactions)
                .add("#!BV/#T", (numberOfFalseBVChecked * 1.0) / numberOfTransactions)
                .add("#!FV/#T", (numberOfFalseFVChecked * 1.0) / numberOfTransactions)
                .add("#!SV/#T", (numberOfFalseSVChecked * 1.0) / numberOfTransactions)
                .toString();
    }

    @Override
    public String toString() {
        return summaryReport();
    }

}
