package messaging.socket;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import messaging.AbstractMessage;
import messaging.IMessageConsumer;
import messaging.IMessageListener2;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * {@link SocketMsgListener2} accepts {@link AbstractMessage}s
 * via sockets and handles with them.
 * @author hengxin
 * @date 16-8-30
 */
public class SocketMsgListener2 implements IMessageListener2 {
    private static final Logger LOGGER = getLogger(SocketMsgListener2.class);
    private static final ExecutorService exec = Executors.newCachedThreadPool();

    @NotNull private final int port;
    private IMessageConsumer consumer;

    public SocketMsgListener2(int port) { this.port = port; }
    public SocketMsgListener2(String spProperties) { port = SocketPortPropertiesUtil.getPort(spProperties); }

    @Override
    public void accept() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket connection = server.accept();
                    LOGGER.debug("Create connection [{}].", connection);
                    exec.submit(new CommitMsgTask(connection, consumer));
                } catch (IOException ioe) {
                    LOGGER.error("Failed to accept connections due to [{}].", ioe);
                    ioe.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            LOGGER.error("Failed to create ServerSocket on port [{}] due to [{}].", port, ioe);
            ioe.printStackTrace();
        }
    }

    @Override
    public void register(@NotNull IMessageConsumer consumer) {
        this.consumer = consumer;
    }

}

class CommitMsgTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommitMsgTask.class);

    @NotNull private final Socket connection;
    @NotNull private final IMessageConsumer consumer;

    CommitMsgTask(@NotNull Socket connection, @NotNull IMessageConsumer consumer) {
        this.connection = connection;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        try (ObjectInputStream ois = new ObjectInputStream(connection.getInputStream())) {
            LOGGER.debug("Get ObjectInputStream: [{}].", ois);
            AbstractMessage msg = (AbstractMessage) ois.readObject();
            LOGGER.debug("Get msg: [{}]. The consumer is [{}].", msg, consumer);
            consumer.consume(msg);
        } catch (IOException ioe) {
            LOGGER.error("Failed to getInputStream() due to [{}].", ioe);
            ioe.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            LOGGER.error("Failed to receive message due to [{}].", cnfe);
            cnfe.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                LOGGER.warn("Failed to close connection [{}].", connection);
            }
        }
    }

}

