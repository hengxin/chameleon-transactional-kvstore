package benchmarking.statistics;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class WorkloadStatistics extends AbstractWorkloadStatistics {
    private final List<AbstractClientStatistics> clientStats = new ArrayList<>();

    @Override
    public void collect(final @Nullable AbstractClientStatistics clientStat) {
        if (clientStat != null) {
            clientStats.add(clientStat);

            numberOfTransactions += clientStat.countAll();
            numberOfCommittedTransactions += clientStat.countCommitted();
            numberOfAbortedTransactions += clientStat.countAborted();
            numberOfFalseVcChecked += clientStat.countFalseVcChecked();
            numberOfFalseBVChecked += clientStat.countFalseBVChecked();
            numberOfFalseFVChecked += clientStat.countFalseFVChecked();
            numberOfFalseSVChecked += clientStat.countFalseSVChecked();
            numberOfFalseWcfChecked += clientStat.countFalseWcfChecked();
        }
    }

    @Override
    public String briefReport() {
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
    public String summaryReport() {
        return clientStats.stream()
                .map(AbstractClientStatistics::summaryReport)
                .collect(Collectors.joining("; ", briefReport(), "."));
    }

}
