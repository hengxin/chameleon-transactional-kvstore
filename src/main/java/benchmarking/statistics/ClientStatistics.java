package benchmarking.statistics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twopc.TransactionCommitResult;

/**
 * @author hengxin
 * @date 16-9-15
 */
public class ClientStatistics extends AbstractClientStatistics {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientStatistics.class);

    @Override
    public void collect(TransactionCommitResult transactionCommitResult) {
        numberOfTransactions++;

        if (transactionCommitResult.isCommitted())
            numberOfCommittedTransactions++;
        else numberOfAbortedTransactions++;

        if (! transactionCommitResult.isBVChecked()) numberOfFalseBVChecked++;
        if (! transactionCommitResult.isFVChecked()) numberOfFalseFVChecked++;
        if (! transactionCommitResult.isSVChecked()) numberOfFalseSVChecked++;

        if (! transactionCommitResult.isVcChecked()) numberOfFalseVcChecked++;
        if (! transactionCommitResult.isWcfChecked()) numberOfFalseWcfChecked++;
    }

}
