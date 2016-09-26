package messaging.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import messaging.AbstractMessage;
import messaging.IMessageProducer;

import static benchmarking.workload.network.NetworkDelayGenerator.simulateInterDCComm;

/**
 * {@link SocketMsgProducer} sends {@link AbstractMessage}
 * via socket.
 * @author hengxin
 * @date 16-8-30
 */
public class SocketMsgProducer implements IMessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketMsgProducer.class);

    private Socket socket;

    public SocketMsgProducer(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (IOException ioe) {
            LOGGER.error("Failed to establish connection to [{}:{}].", host, port);
            ioe.printStackTrace();
        }
    }

    public SocketMsgProducer(SocketAddress address) {
        socket = new Socket();
        try {
            socket.connect(address);
        } catch (IOException ioe) {
            LOGGER.error("Failed to establish connection to [{}].", address);
        }
    }

    @Override
    public void send(AbstractMessage msg) {
        try ( ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
            simulateInterDCComm();

            oos.writeObject(msg);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            LOGGER.error("Failed to write due to [{}].", ioe);
        }
    }
}
