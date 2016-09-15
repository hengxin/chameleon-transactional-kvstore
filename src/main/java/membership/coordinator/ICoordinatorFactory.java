package membership.coordinator;

import org.jetbrains.annotations.NotNull;

import java.rmi.Remote;
import java.rmi.RemoteException;

import client.context.AbstractClientContext;
import twopc.coordinator.Abstract2PCCoordinator;
import twopc.coordinator.RVSI2PCPhaserCoordinator;

/**
 * @author hengxin
 * @date 16-8-17
 */
public interface ICoordinatorFactory extends Remote {
    /**
     * @param ctx {@link AbstractClientContext}
     * @return an instance of {@link RVSI2PCPhaserCoordinator}
     * @throws RemoteException
     */
    @NotNull Abstract2PCCoordinator getRVSI2PCPhaserCoord(AbstractClientContext ctx)
            throws RemoteException;
}
