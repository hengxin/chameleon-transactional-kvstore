package client.workload.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

import client.workload.transaction.ITransactionGenerator;

/**
 * {@link ClientWorkloadGenerator} generates transactions
 * for each individual client.
 * @author hengxin
 * @date 16-9-7
 */
public class ClientWorkloadGenerator implements IClientWorkloadGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientWorkloadGenerator.class);

    private final int numberOfTransactions;
//    private final ITransactionSizeGenerator transactionSizeGenerator;
    private final ITransactionGenerator transactionGenerator;

    public ClientWorkloadGenerator(final int numberOfTransactions,
//                                   final ITransactionSizeGenerator transactionSizeGenerator,
                                   final ITransactionGenerator transactionGenerator) {
        this.numberOfTransactions = numberOfTransactions;
//        this.transactionSizeGenerator = transactionSizeGenerator;
        this.transactionGenerator = transactionGenerator;
    }

    @Override
    public ClientWorkload generate() {
        ClientWorkload clientWorkload = new ClientWorkload(numberOfTransactions);
        IntStream.range(0, numberOfTransactions)
                .forEach(index -> clientWorkload.addTransaction(transactionGenerator.generate()));

        return clientWorkload;
    }

}
