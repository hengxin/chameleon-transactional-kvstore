package messaging.socket;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import client.clientlibrary.transaction.BufferedUpdates;
import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.component.Timestamp;
import messaging.IMessageListener2;
import messaging.IMessageProducer;

/**
 * {@link SocketMsgBroadcastProducerTest} test {@link SocketMsgBroadcastProducer}.
 *
 * @author hengxin
 * @date 16-9-1
 */
public class SocketMsgBroadcastProducerTest {
    private static final String HOST = "localhost";
    @NotNull
    private static ExecutorService exec = Executors.newCachedThreadPool();

    private static final int PORT_1111 = 1111;
    private static final int PORT_2222 = 2222;
    private static final int PORT_3333 = 3333;

    private IMessageProducer broadcaster;

    @NotNull
    private IMessageListener2 listener1111 = new SocketMsgListener2(PORT_1111);
    @NotNull
    private IMessageListener2 listener2222 = new SocketMsgListener2(PORT_2222);
    @NotNull
    private IMessageListener2 listener3333 = new SocketMsgListener2(PORT_3333);

    @Before
    public void setUp() throws Exception {
        List<SocketAddress> addresses = new ArrayList<>();
        addresses.add(new InetSocketAddress(HOST, PORT_1111));
        addresses.add(new InetSocketAddress(HOST, PORT_2222));
        addresses.add(new InetSocketAddress(HOST, PORT_3333));

        List<IMessageListener2> listener2s = new ArrayList<>();
        listener2s.add(listener1111);
        listener2s.add(listener2222);
        listener2s.add(listener3333);

        for (IMessageListener2 listener : listener2s)
            exec.submit(() -> {
                listener.register(System.out::println);
                listener.accept();
            });

        TimeUnit.SECONDS.sleep(2);

        broadcaster = new SocketMsgBroadcastProducer(addresses);

        TimeUnit.SECONDS.sleep(5);
    }

        @Test
    public void send() throws Exception {
        ToCommitTransaction tx = new ToCommitTransaction(new Timestamp(0L), new BufferedUpdates());
        broadcaster.send(tx);
    }

}