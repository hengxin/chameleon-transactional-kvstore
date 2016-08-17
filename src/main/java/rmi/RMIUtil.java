package rmi;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;

import exception.rmi.RMIRegistryException;
import membership.site.Member;

import static java.rmi.registry.LocateRegistry.getRegistry;
import static rmi.IRMI.RMI_REGISTRY_PORT;

/**
 * {@link RMIUtil} is a utility class for exporting and reclaiming remote objects.
 * @author hengxin
 * @date 16-8-17
 */
public final class RMIUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(RMIUtil.class);
    private static Registry rmiRegistry;

    /**
     * The following "create-exception-get" pattern avoids creating registry twice
     * on the same port in a single host.
     *
     * @see <a ref="https://community.oracle.com/thread/2082536?start=0&tstart=0">
     *     How to check if RMI Registry is already running?</a>
     */
    static {
        try {
            rmiRegistry = LocateRegistry.createRegistry(RMI_REGISTRY_PORT);
        } catch (RemoteException e) {
            try {
                rmiRegistry = getRegistry(RMI_REGISTRY_PORT);
            } catch (RemoteException re) {
                LOGGER.error("Failed to create/get RMI Registry on port [{}].", RMI_REGISTRY_PORT);
                re.printStackTrace();
            }
        }
    }

    public static void export(Remote obj, String host, int port, String name) {
        System.setProperty("java.rmi.server.hostname", host);

        try {
            Remote remote = UnicastRemoteObject.exportObject(obj, port);
            getRegistry().rebind(name, remote);
            LOGGER.info("The object [{}] has successfully exported itself as [{}] for remote accesses.", obj, remote);
        } catch (RemoteException re) {
            throw new RMIRegistryException(String.format("Failed to export this object [%s] for remote accesses.", obj)
                    , re.getCause());
        }
    }

    @Nullable
    public static Remote lookup(String memberStr) {
        Optional<Member> member = Member.parseMember(memberStr);
        if (member.isPresent())
            return lookup(member.get());
        else return null;
    }

    @Nullable
    public static Remote lookup(Member member) {
        return lookup(member.getHost(), member.getRmiRegistryPort(), member.getRmiRegistryName());
    }

    /**
     * Look up remote object via RMI.
     * @param host
     * @param port
     * @param name
     * @return a {@link Remote} object; it could be {@code null}.
     */
    @Nullable
    public static Remote lookup(String host, int port, String name) {
        try {
            Remote obj = getRegistry(host, port).lookup(name);
            LOGGER.info("Successfully locate [{}] via RMI.", obj);
            return obj;
        } catch (RemoteException | NotBoundException renbe) {
            LOGGER.warn("Cannot locate [{}] via RMI.", name);
            return null;
        }
    }

}
