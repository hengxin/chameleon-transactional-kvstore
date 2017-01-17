package messaging.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import conf.SiteConfig;
import conf.SiteConfig.SiteConfigKey;
import utils.PropertiesUtil;

/**
 * {@link SocketPortPropertiesUtil} parses the socket port properties file (sp.properties).
 * @author hengxin
 * @date 16-9-1
 */
public class SocketPortPropertiesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketPortPropertiesUtil.class);

    /**
     * Parse properties file for socket port.
     * @param spProperties
     * @return port; it is {@value SiteConfig#DEFAULT_SOCKET_PORT} if error occurs.
     */
    public static int getPort(String spProperties) {
        int port;
        try {
            Properties prop = PropertiesUtil.load(spProperties);
            port = Integer.parseInt(prop.getProperty(SiteConfigKey.SOCKET_PORT.getConfigKey()));
        } catch (IOException ioe) {
            LOGGER.warn("Failed to load sp.properties [{}]. Use the default socket port [{}]",
                    spProperties, SiteConfig.DEFAULT_SOCKET_PORT);
            port = SiteConfig.DEFAULT_SOCKET_PORT;
        }

        return port;
    }
}
