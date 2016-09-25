package membership.coordinator;

import org.jetbrains.annotations.NotNull;
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

import static conf.SiteConfig.DEFAULT_CF_PROPERTIES;
import static conf.SiteConfig.DEFAULT_TO_PROPERTIES;

/**
 * {@link CoordinatorFactory} creates {@link Abstract2PCCoordinator} instances.
 * @author hengxin
 * @date 16-8-16
 */
public class CoordinatorFactory implements ICoordinatorFactory, IRMI {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoordinatorFactory.class);

    private Member self;
    private final String toProperties;

    public CoordinatorFactory() { this(DEFAULT_CF_PROPERTIES, DEFAULT_TO_PROPERTIES); }

    public CoordinatorFactory(String cfProperties, String toProperties) {
        this.toProperties = toProperties;

        try {
            Properties prop = PropertiesUtil.load(cfProperties);
            Set<String> idStrs = prop.stringPropertyNames();
            String idStr = idStrs.toArray(new String[idStrs.size()])[0];
            self = Member.parseMember(prop.getProperty(idStr)).get();
            export();
            LOGGER.info("[{}:{}] has been successfully launched.", CoordinatorFactory.class.getSimpleName(), self);
        } catch (IOException ioe) {
            LOGGER.error("Failed to parse [{}] from properties [{}]", this.getClass().getSimpleName(), cfProperties);
        }
    }

    @NotNull
    @Override
    public Abstract2PCCoordinator getRVSI2PCPhaserCoord(AbstractClientContext ctx)
            throws RemoteException {
        return new RVSI2PCPhaserCoordinator(ctx, toProperties);
    }

    @Override
    public void export() { RMIUtil.export(this, self.getHost(), self.getPort(), self.getRmiRegistryName()); }

    @Override
    public void reclaim() {

    }
}
