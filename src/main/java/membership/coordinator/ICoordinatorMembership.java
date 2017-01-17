package membership.coordinator;

import org.jetbrains.annotations.Nullable;

import client.context.AbstractClientContext;
import twopc.coordinator.Abstract2PCCoordinator;

/**
 * @author hengxin
 * @date 16-8-17
 */
public interface ICoordinatorMembership {
    @Nullable Abstract2PCCoordinator getCoord(int coordId, AbstractClientContext ctx);
}
