package messaging.socket;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import messaging.AbstractMessage;
import messaging.IMessageConsumer;
import messaging.IMessageListener2;

/**
 * {@link SocketMsgListener2Test} tests {@link SocketMsgListener2}.
 * It works together with {@link SocketMsgProducerTest}.
 *
 * Run {@link SocketMsgListener2Test} first,
 * and then run {@link SocketMsgProducerTest}.
 *
 * @author hengxin
 * @date 16-9-1
 */
public class SocketMsgListener2Test {
    static final String TEST_HOST = "127.0.0.1";
    static final int TEST_PORT = 11111;

    private IMessageListener2 listener;

    @Before
    public void setUp() throws Exception {
        listener = new SocketMsgListener2(TEST_PORT);
        IMessageConsumer consumer = new CommitMsgConsumerStub();
        listener.register(consumer);
    }

    @Test
    public void accept() throws Exception {
        listener.accept();
    }

    private static class CommitMsgConsumerStub implements IMessageConsumer {
        private static final Logger LOGGER = LoggerFactory.getLogger(CommitMsgConsumerStub.class);

        @Override
        public void consume(AbstractMessage msg) {
           LOGGER.info("Consume msg [{}].", msg);
        }
    }

}