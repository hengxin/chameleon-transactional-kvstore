package main;

import exception.transaction.TransactionBeginException;

/**
 * {@link ClientMainTest} test clients who issue transactions.
 * @author hengxin
 * @date 16-9-2
 */
public class ClientMainTest {
    public static void main(String[] args) throws TransactionBeginException {
        String siteProperties = args[0];
        String cfProperties = args[1];
        String toProperties = args[2];

        ClientLauncher client = new ClientLauncher(siteProperties, cfProperties, toProperties);
        client.testTx();
    }
}
