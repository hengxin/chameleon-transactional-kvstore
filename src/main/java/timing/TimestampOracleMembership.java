package timing;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

import rmi.RMIUtil;
import utils.PropertiesUtil;

/**
 * @description {@link TimestampOracleMembership} is to
 *  locate remote {@link ITimestampOracle} object.
 * @author hengxin
 * @date 16-8-30
 */
public class TimestampOracleMembership {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimestampOracleMembership.class);
    private final String toProperties;

    public TimestampOracleMembership(String toProperties) {
        this.toProperties = toProperties;
    }

    @Nullable
    public ITimestampOracle locateTO() {
        try {
            Properties prop = PropertiesUtil.load(toProperties);
            // only one line in the {@code toProperties} file
            String toMemberStr = prop.getProperty(prop.stringPropertyNames().toArray(new String[1])[0]);
            return (ITimestampOracle) RMIUtil.lookup(toMemberStr);
        } catch (IOException e) {
            LOGGER.error("Cannot locate [{}] from [{}].",
                    getClass().getSimpleName(), toProperties);
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

}
