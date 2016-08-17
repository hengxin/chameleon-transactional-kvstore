package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * {@link PropertiesUtil} provides utility methods for manipulating properties files.
 * @author hengxin
 * @date 16-6-9
 */
public final class PropertiesUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtil.class);

    public static Properties load(final String properties)
            throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        Properties props = new Properties();
        try (InputStream is = classLoader.getResourceAsStream(properties)) {
            props.load(is);
            LOGGER.info("Load the properties file [{}] successfully.", properties);
        } catch (IOException ioe) {
            throw new IOException(ioe);
        }

        return props;
    }
}
