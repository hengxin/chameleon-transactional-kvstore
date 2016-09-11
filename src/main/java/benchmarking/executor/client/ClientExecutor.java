package benchmarking.executor.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import benchmarking.executor.transaction.ITransactionExecutor;
import benchmarking.workload.client.ClientWorkload;
import benchmarking.workload.transaction.Transaction;
import client.context.AbstractClientContext;

/**
 * @author hengxin
 * @date 16-9-11
 */
public class ClientExecutor implements IClientExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientExecutor.class);

    private final AbstractClientContext cctx;
    private final ITransactionExecutor transactionExecutor;

    public ClientExecutor(final AbstractClientContext cctx,
                          final ITransactionExecutor transactionExecutor) {
        this.cctx = cctx;
        this.transactionExecutor = transactionExecutor;
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
                .forEachOrdered(transactionExecutor::execute);
    }

}
