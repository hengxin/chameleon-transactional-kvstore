package benchmarking.executor.client;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import benchmarking.executor.IInterIssueTimeGenerator;
import benchmarking.executor.transaction.ITransactionExecutor;
import benchmarking.statistics.IClientStatistics;
import benchmarking.workload.client.ClientWorkload;
import benchmarking.workload.transaction.Transaction;

/**
 * @author hengxin
 * @date 16-9-11
 */
public class ClientExecutor implements IClientExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientExecutor.class);

    private final ITransactionExecutor transactionExecutor;
    private final IInterIssueTimeGenerator interIssueTimeGenerator;

    @Nullable
    private final IClientStatistics clientStat;

    public ClientExecutor(final ITransactionExecutor transactionExecutor,
                          final IInterIssueTimeGenerator interIssueTimeGenerator,
                          final @Nullable IClientStatistics clientStat) {
        this.transactionExecutor = transactionExecutor;
        this.interIssueTimeGenerator = interIssueTimeGenerator;

        this.clientStat = clientStat;
    }

    /**
     * Execute all {@link Transaction}s
     * in this client one at a time in encounter order.
     * @param clientWorkload {@link ClientWorkload} to be executed;
     *      it consists of a list of {@link Transaction}s.
     */
    @Override
    public void execute(ClientWorkload clientWorkload) {
        clientWorkload.getTxs().stream()
                .forEachOrdered(tx -> {
                    long interval = interIssueTimeGenerator.generate();
                    try {
                        LOGGER.debug("Sleep for [{}] ms.", interval);
                        TimeUnit.MILLISECONDS.sleep(interval);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                    boolean committed = transactionExecutor.execute(tx);

                    if (clientStat != null)
                        if (committed)
                            clientStat.incCommitted();
                        else clientStat.incAborted();
                });
    }

    @Override
    public @Nullable IClientStatistics getClientStat() {
        return clientStat;
    }

}
