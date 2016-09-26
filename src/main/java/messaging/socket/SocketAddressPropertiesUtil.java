package messaging.socket;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import utils.PropertiesUtil;

/**
 * {@link SocketAddressPropertiesUtil} parses the sa.properties file
 * to get a list of {@link SocketAddress}es.
 *
 * @author hengxin
 * @date 16-9-1
 */
public class SocketAddressPropertiesUtil {

    public static List<SocketAddress> getAddresses(String saProperties) {
        List<SocketAddress> addresses = new ArrayList<>();

        try {
            Properties prop = PropertiesUtil.load(saProperties);
            addresses = prop.stringPropertyNames().stream()
                    .map(prop::getProperty)
                    .map(SocketAddressPropertiesUtil::parse)
                    .collect(Collectors.toList());

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return addresses;
    }

    @NotNull
    private static SocketAddress parse(@NotNull String hostIp) {
        String[] parts = hostIp.split(":");
        return new InetSocketAddress(parts[0], Integer.parseInt(parts[1]));
    }

}
