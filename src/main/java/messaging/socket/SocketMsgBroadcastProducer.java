package messaging.socket;

import org.jetbrains.annotations.NotNull;

import java.net.SocketAddress;
import java.util.List;

import messaging.AbstractMessage;
import messaging.IMessageProducer;

/**
 * {@link SocketMsgBroadcastProducer} broadcast messages
 * via sockets.
 *
 * @author hengxin
 * @date 16-9-1
 */
public class SocketMsgBroadcastProducer implements IMessageProducer {
    private final List<SocketAddress> addresses;

    public SocketMsgBroadcastProducer(@NotNull String saProperties) {
        this.addresses = SocketAddressPropertiesUtil.getAddresses(saProperties);
    }

    public SocketMsgBroadcastProducer(List<SocketAddress> addresses) {
        this.addresses = addresses;
    }

    @Override
    public void send(AbstractMessage msg) {
        addresses.stream()
                .map(SocketMsgProducer::new)
                .forEach(producer -> producer.send(msg));
    }

}
