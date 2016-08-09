package site;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.stream.Collectors;

import context.AbstractContext;
import exception.rmi.RMIRegistryException;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;
import network.membership.Member;
import network.membership.RuntimeMember;
import rmi.IRMI;

import static java.rmi.registry.LocateRegistry.getRegistry;

/**
 * An {@link AbstractSite} holds an {@link AbstractTable}, upon which
 * it provides remotely available {@code get/put} operations (by implementing {@link ISite}).
 *
 * An {@link AbstractSite} exports itself for RMI calls by implementing {@link IRMI}.
 *
 * @author hengxin
 * @date Created on 11-25-2015
 *
 * FIXME separate the rmi logic (export/unexport) from the table logic (get/put)???
 */
public abstract class AbstractSite implements ISite, IRMI {

	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractSite.class);
    private static Registry rmiRegistry;

    /**
     * The following "create-exception-get" pattern avoids creating registry twice
     * on the same port in a single host.
     *
     * @see <a ref="https://community.oracle.com/thread/2082536?start=0&tstart=0">How to check if RMI Registry is already running?</a>
     */
    static {
        try {
            rmiRegistry = LocateRegistry.createRegistry(RMI_REGISTRY_PORT);
        } catch (RemoteException e) {
            try {
                rmiRegistry = LocateRegistry.getRegistry(RMI_REGISTRY_PORT);
            } catch (RemoteException re) {
                LOGGER.error("Failed to create/get RMI Registry on port [{}].", RMI_REGISTRY_PORT);
                re.printStackTrace();
            }
        }
    }

    private final Member self;
	protected final AbstractContext context;
	protected AbstractTable table;
	
	public AbstractSite(AbstractContext context) {
		this.context = context;
		self = context.getMembership().getSelf();
        export();
	}
	
	/**
	 * FIXME Is it appropriate for {@link #get(Row, Column)} here?
	 */
	@Override
	public ITimestampedCell get(Row r, Column c) {
		return this.table.getTimestampedCell(r, c);
	}
	
	/**
	 * FIXME implementation? Is it appropriate for {@link #put(Row, Column, ITimestampedCell)} here?
	 */
	@Override
	public boolean put(Row r, Column c, ITimestampedCell ts_cell) {
		return false;
	}

	/**
	 * Export self for remote accesses via RMI.
	 */
	@Override
	public void export() {
		System.setProperty("java.rmi.server.hostname", self.getHost());

		try {
			Remote remote = UnicastRemoteObject.exportObject(this, self.getPort());
            LocateRegistry.getRegistry().rebind(self.getRmiRegistryName(), remote);
			LOGGER.info("The site [{}] has successfully exported itself as [{}] for remote accesses.", self, remote);
		} catch (RemoteException re) {
            throw new RMIRegistryException(String.format("Failed to export self [%s] for remote accesses.", self), re.getCause());
        }
    }
	
	/**
	 * @implNote 
	 * 	@deprecated FIXME Currently this implementation is buggy. Don't call it in your code.
	 */
	@Override
	public void reclaim() {
//		try {
//			getRegistry(this.self.getHost(), this.self.getRmiRegistryPort()).unbind(this.self.getRmiRegistryName());
//		} catch (RemoteException | NotBoundException e) {
//			throw new RMIRegistryException(String.format("Failed to reclaim self (%s) from remote access.", this.self), e.getCause());
//		}
	}
	
	/**
	 * Locate the stub for the {@link Member}; used later for RMI invocation.
	 * @param member an {@link Member} representing a site
     * @return instance of {@link RuntimeMember}
	 */
    public static RuntimeMember locateRuntimeMember(Member member) {
        try {
            ISite site = (ISite) getRegistry(member.getHost(), member.getRmiRegistryPort())
                    .lookup(member.getRmiRegistryName());
            LOGGER.info("Successfully locate [{}] via RMI.", member);
            return new RuntimeMember(member, site);
        } catch (RemoteException | NotBoundException e) {
            LOGGER.warn("Cannot locate [{}] via RMI.", member);
            return new RuntimeMember(member, null);
        }
    }

	/**
	 * Locate the stubs for a list of {@link Member}; used later for RMI invocation.
	 * @param members a list of {@link Member}s to be located.
	 * @return 	a list of {@link RuntimeMember}
	 */
    public static List<RuntimeMember> locateRuntimeMembers(List<Member> members) {
        return members.stream()
                .map(AbstractSite::locateRuntimeMember)
                .collect(Collectors.toList());
    }

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(this.self)
				.toString();
	}

}
