package membership.coordinator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import client.context.AbstractClientContext;
import rmi.RMIUtil;
import twopc.coordinator.Abstract2PCCoordinator;
import util.PropertiesUtil;

/**
 * {@link CoordinatorMembership} maintains a collection of {@link CoordinatorFactory}s
 * which are responsible for creating {@link twopc.coordinator.Abstract2PCCoordinator}s.
 *
 * @author hengxin
 * @date 16-8-17
 */
public class CoordinatorMembership implements ICoordinatorMembership {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoordinatorMembership.class);
    public static final String DEFAULT_COORD_FACTORY_PROPERTIES_FILE = "membership/coordinator/cf.properties";

    private final Map<Integer, CoordinatorFactory> cfMap = new HashMap<>();

    /**
     * Constructor with default {@value #DEFAULT_COORD_FACTORY_PROPERTIES_FILE}
     */
    public CoordinatorMembership() {
        this(DEFAULT_COORD_FACTORY_PROPERTIES_FILE);
    }

    public CoordinatorMembership(String properties) {
        try {
            Properties prop = PropertiesUtil.load(properties);

            prop.stringPropertyNames().stream()
                    .map(idStr -> new CFMember(idStr, prop.getProperty(idStr)))
                    .collect(Collectors.toMap(CFMember::getId, CFMember::getCf));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Abstract2PCCoordinator getCoord(int coordId, AbstractClientContext ctx) {
        try {
            return cfMap.get(coordId).getRVSI2PCPhaserCoord(ctx);
        } catch (RemoteException re) {
            LOGGER.error("Failed to get Coordinator due to \n[{}]", re.getMessage());
            System.exit(1);  // FIXME
            return null;
        }
    }

    private class CFMember {
        private int id;
        private CoordinatorFactory cf;

        CFMember(String idStr, String memberStr) {
            id = Integer.parseInt(idStr);
            cf = (CoordinatorFactory) RMIUtil.lookup(memberStr);
        }

        int getId() { return id; }
        CoordinatorFactory getCf() { return cf; }
    }
}
