package conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

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

    public enum SiteConfigKey {
        HOST("host"),
        PORT("port"),
        RMI_REGISTRY_NAME("rmiRegistryName"),
        RMI_REGISTRY_PORT("rmiRegistryPort");

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
    public String getConfigValue(final SiteConfigKey configKey) {
        return props.getProperty(configKey.getConfigKey());
    }
}
