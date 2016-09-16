package conf;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import util.PropertiesUtil;

/**
 * {@link SiteConfig} collects configuration parameters for sites.
 * There are *** types of configuration parameters:
 * <ul>
 *     <li>RMI related</li>
 *     <li>Table related</li>
 * </ul>
 *
 * @author hengxin
 * @date 16-6-9
 */
public final class SiteConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(SiteConfig.class);

    // the following is for timestamp oracle
    @Language("Properties")
    public static final String DEFAULT_TO_PROPERTIES = "timing/to.properties";

    // the following are for site membership
    @Language("Properties")
    public static final String DEFAULT_MASTER_SITE_PROPERTIES = "master/site.properties";
    @Language("Properties")
    public final static String DEFAULT_SLAVE_SITE_PROPERTIES = "slave/site.properties";

    // the following are for socket communication
    @Language("Properties")
    public static final String DEFAULT_SOCKET_ADDRESS_PROPERTIES = "messaging/socket/sa.properties";
    @Language("Properties")
    public static final String DEFAULT_SOCKET_PORT_PROPERTIES = "messaging/socket/sp.properties";
    public static final int DEFAULT_SOCKET_PORT = 1111;

    // for simulation
    public static boolean IS_IN_SIMULATION_MODE = false;
    private static int intraDCDelay = 5;
    private static int interDCDelay = 30;
    public static final NormalDistribution INTRA_DC_NORMAL_DIST = new NormalDistribution(intraDCDelay, 1);
    public static final NormalDistribution INTER_DC_NORMAL_DIST = new NormalDistribution(interDCDelay, 1);

    public enum SiteConfigKey {
        HOST("host"),
        PORT("port"),
        RMI_REGISTRY_NAME("rmiRegistryName"),
        RMI_REGISTRY_PORT("rmiRegistryPort"),
        SOCKET_PORT("socket-port");

        private final String configKey;
        SiteConfigKey(final String configKey) {
            this.configKey = configKey;
        }

        public String getConfigKey() {
            return configKey;
        }
    }

    private Properties props;

    public SiteConfig(final String properties) {
        try {
            props = PropertiesUtil.load(properties);
        } catch (IOException ioe) {
            LOGGER.error("Failed to load the site configuration file [{}] due to [{}].", properties, ioe.toString());
            System.exit(1);
        }
    }

    /**
     * @param configKey enum {@link SiteConfigKey}
     * @return  configuration value; possibly <code>null</code>  // TODO handle with null
     */
    public String getConfigValue(@NotNull final SiteConfigKey configKey) {
        return props.getProperty(configKey.getConfigKey());
    }

    public static void simulateInterDCComm() {
        if (SiteConfig.IS_IN_SIMULATION_MODE)
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(SiteConfig.INTER_DC_NORMAL_DIST.sample()));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
    }

    public static void simulateIntraDCComm() {
        if (SiteConfig.IS_IN_SIMULATION_MODE)
            try {
                TimeUnit.MILLISECONDS.sleep(Math.round(SiteConfig.INTRA_DC_NORMAL_DIST.sample()));
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
    }

}
