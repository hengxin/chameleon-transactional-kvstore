package messaging.socket;

import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hengxin
 * @date 16-9-1
 */
public class SocketAddressPropertiesUtilTest {
    private static final String HOST = "127.0.0.1";
    private static final int PORT_11111 = 11111;
    private static final int PORT_22222 = 22222;
    private static final int PORT_33333 = 33333;

    private static final String SA_PROPERTIES = "messaging/socket/sa.properties";

    @NotNull
    private List<SocketAddress> expectedAddresses = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        SocketAddress address1 = new InetSocketAddress(HOST, PORT_11111);
        SocketAddress address2 = new InetSocketAddress(HOST, PORT_22222);
        SocketAddress address3 = new InetSocketAddress(HOST, PORT_33333);

        expectedAddresses.add(address1);
        expectedAddresses.add(address2);
        expectedAddresses.add(address3);
    }

    @Test
    public void getAddresses() throws Exception {
        List<SocketAddress> addresses = SocketAddressPropertiesUtil.getAddresses(SA_PROPERTIES);
        Assert.assertTrue("Two address lists should be the same.",
                CollectionUtils.isEqualCollection(expectedAddresses, addresses));
    }

}