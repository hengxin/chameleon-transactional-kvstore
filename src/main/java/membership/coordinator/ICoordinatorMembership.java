package membership.coordinator;

import client.context.AbstractClientContext;
import twopc.coordinator.Abstract2PCCoordinator;

/**
 * @author hengxin
 * @date 16-8-17
 */
public interface ICoordinatorMembership {
    Abstract2PCCoordinator getCoord(int coordId, AbstractClientContext ctx);
}
