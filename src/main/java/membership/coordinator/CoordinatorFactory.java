package membership.coordinator;

import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Set;

import client.context.AbstractClientContext;
import membership.site.Member;
import rmi.IRMI;
import rmi.RMIUtil;
import twopc.coordinator.Abstract2PCCoordinator;
import twopc.coordinator.RVSI2PCPhaserCoordinator;
import util.PropertiesUtil;

/**
 * {@link CoordinatorFactory} creates {@link Abstract2PCCoordinator} instances.
 * @author hengxin
 * @date 16-8-16
 */
public class CoordinatorFactory implements ICoordinatorFactory, IRMI {
    private transient static final Logger LOGGER = LoggerFactory.getLogger(CoordinatorFactory.class);
    @Language("Properties")
    private static final String DEFAULT_CF_PROPERTIES = "membership/coordinator/cf.properties";

    private Member self;

    public CoordinatorFactory() { this(DEFAULT_CF_PROPERTIES); }

    public CoordinatorFactory(String cfProperties) {
        try {
            Properties prop = PropertiesUtil.load(cfProperties);
            Set<String> idStrs = prop.stringPropertyNames();
            String idStr = idStrs.toArray(new String[idStrs.size()])[0];
            self = Member.parseMember(prop.getProperty(idStr)).get();
            export();
        } catch (IOException ioe) {
            LOGGER.error("Failed to parse CoordinatorFactory from properties [{}]", cfProperties);
        }
    }

    @Override
    public Abstract2PCCoordinator getRVSI2PCPhaserCoord(AbstractClientContext ctx)
            throws RemoteException {
        LOGGER.debug("[{}] will return an RVSI2PCPhaserCoordinator.", this.getClass().getSimpleName());
        return new RVSI2PCPhaserCoordinator(ctx);
    }

    @Override
    public void export() {
        RMIUtil.export(this, self.getHost(), self.getPort(), self.getRmiRegistryName());
    }

    @Override
    public void reclaim() {

    }
}
