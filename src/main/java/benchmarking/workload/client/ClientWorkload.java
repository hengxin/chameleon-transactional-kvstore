package benchmarking.workload.client;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import benchmarking.workload.transaction.Transaction;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@link ClientWorkload} is workload on per client.
 *
 * @author hengxin
 * @date 16-9-7
 */
public class ClientWorkload {
    private static final Logger LOGGER = getLogger(ClientWorkload.class);

    @NotNull
    private final List<Transaction> txs;

    public ClientWorkload(final int numberOfTransactions) {
        txs = new ArrayList<>(numberOfTransactions);
    }

    public void addTransaction(Transaction tx) { txs.add(tx); }

    @NotNull
    public List<Transaction> getTxs() { return txs; }

    @NotNull
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("list of transactions", txs)
                .toString();
    }

}
