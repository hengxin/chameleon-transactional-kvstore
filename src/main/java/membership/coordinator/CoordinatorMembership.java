package membership.coordinator;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
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
public class CoordinatorMembership implements ICoordinatorMembership, Serializable {
    private static final long serialVersionUID = -6310920185254879511L;
    private static final Logger LOGGER = LoggerFactory.getLogger(CoordinatorMembership.class);

    private Map<Integer, ICoordinatorFactory> cfMap = new HashMap<>();

    public CoordinatorMembership(String properties) {
        try {
            Properties prop = PropertiesUtil.load(properties);

            cfMap = prop.stringPropertyNames().stream()
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
        private ICoordinatorFactory cf;

        CFMember(String idStr, String memberStr) {
            id = Integer.parseInt(idStr);
            cf = (ICoordinatorFactory) RMIUtil.lookup(memberStr);
        }

        int getId() { return id; }
        ICoordinatorFactory getCf() { return cf; }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("id", id)
                    .addValue(cf)
                    .toString();
        }
    }
}
