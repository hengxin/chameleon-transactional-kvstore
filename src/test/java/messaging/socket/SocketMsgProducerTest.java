package messaging.socket;

import org.junit.Before;
import org.junit.Test;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.component.Timestamp;

import static messaging.socket.SocketMsgListener2Test.TEST_HOST;
import static messaging.socket.SocketMsgListener2Test.TEST_PORT;

/**
 * {@link SocketMsgProducerTest} test the class {@link SocketMsgProducer}.
 * It works together with {@link SocketMsgListener2Test}.
 *
 * Run {@link SocketMsgListener2Test} first,
 * and then run {@link SocketMsgProducerTest}.
 *
 * @author hengxin
 * @date 16-9-1
 */
public class SocketMsgProducerTest {
    private SocketMsgProducer producer;

    @Before
    public void setUp() throws Exception {
        producer = new SocketMsgProducer(TEST_HOST, TEST_PORT);
    }

    @Test
    public void send() throws Exception {
        ToCommitTransaction tx = new ToCommitTransaction(new Timestamp(0L), new BufferedUpdates());
        producer.send(tx);
    }

}