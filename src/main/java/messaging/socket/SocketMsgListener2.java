package messaging.socket;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

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

    private final int port;
    @NotNull private IMessageConsumer consumer;

    public SocketMsgListener2(int port) { this.port = port; }

    public SocketMsgListener2(String spProperties) {
        this.port = SocketPortPropertiesUtil.getPort(spProperties);
    }

    @Override
    public void accept() {
        try (ServerSocket server = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket connection = server.accept();
                    LOGGER.info("Create connection [{}].", connection);
                    exec.submit(new CommitMsgTask(connection, consumer));
                } catch (IOException ioe) {
                    LOGGER.error("Failed to accept connections due to [{}].", ioe);
                }
            }
        } catch (IOException ioe) {
            LOGGER.error("Failed to create ServerSocket on port [{}] due to [{}].", port, ioe);
        }
    }

    @Override
    public void register(@NotNull IMessageConsumer consumer) {
        this.consumer = consumer;
    }

}

class CommitMsgTask implements Runnable {
    private static final Logger LOGGER = getLogger(CommitMsgTask.class);

    @NotNull private final Socket connection;
    @NotNull private final IMessageConsumer consumer;

    CommitMsgTask(@NotNull Socket connection, @NotNull IMessageConsumer consumer) {
        this.connection = connection;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        LOGGER.info("Processing this connection [{}].", connection);

        try (ObjectInputStream ois = new ObjectInputStream(connection.getInputStream())) {
            AbstractMessage msg = (AbstractMessage) ois.readObject();
            consumer.consume(msg);
        } catch (IOException ioe) {
            LOGGER.error("Failed to getInputStream() due to [{}].", ioe);
        } catch (ClassNotFoundException cnfe) {
            LOGGER.error("Failed to receive message due to [{}].", cnfe);
        } finally {
            try {
                connection.close();
            } catch (IOException e) {
                LOGGER.info("Failed to close connection [{}].", connection);
            }
        }
    }

}

