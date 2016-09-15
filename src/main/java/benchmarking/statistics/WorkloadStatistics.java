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
public class WorkloadStatistics implements IWorkloadStatistics {
    private final List<IClientStatistics> clientStats = new ArrayList<>();

    @Override
    public void collect(@Nullable final IClientStatistics clientStat) {
        if (clientStat != null)
            clientStats.add(clientStat);
    }

    @Override
    public int countCommitted() {
        return clientStats.stream()
                .mapToInt(IClientStatistics::countCommitted)
                .sum();
    }

    @Override
    public int countAborted() {
        return clientStats.stream()
                .mapToInt(IClientStatistics::countAborted)
                .sum();
    }

    @Override
    public int countAll() {
        return clientStats.stream()
                .mapToInt(IClientStatistics::countAll)
                .sum();
    }

    @Override
    public String summaryReport() {
        int numberOfCommitted = countCommitted();
        int numberOfAborted = countAborted();
        int numberOfAll = countAll();

        String overviewReport = MoreObjects.toStringHelper(this)
                .add("#C", numberOfCommitted)
                .add("#A", numberOfAborted)
                .add("#T", numberOfAll)
                .add("#C/#T", (numberOfCommitted * 1.0) / numberOfAll)
                .add("#A/#T", (numberOfAborted * 1.0)/ numberOfAll)
                .toString();

        return clientStats.stream()
                .map(IClientStatistics::summaryReport)
                .collect(Collectors.joining("; ", overviewReport, "."));
    }

}
