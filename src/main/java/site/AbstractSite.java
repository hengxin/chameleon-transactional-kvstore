package site;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.Remote;
import java.util.List;
import java.util.stream.Collectors;

import context.AbstractContext;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;
import kvs.table.AbstractTable;
import membership.site.Member;
import membership.site.RuntimeMember;
import rmi.IRMI;
import rmi.RMIUtil;

/**
 * An {@link AbstractSite} holds an {@link AbstractTable}, upon which
 * it provides remotely available {@code lookup/put} operations (by implementing {@link ISite}).
 *
 * An {@link AbstractSite} exports itself for RMI calls by implementing {@link IRMI}.
 *
 * @author hengxin
 * @date Created on 11-25-2015
 *
 * FIXME separate the rmi logic (export/unexport) from the table logic (lookup/put)???
 */
public abstract class AbstractSite implements ISite, IRMI {
	private final static Logger LOGGER = LoggerFactory.getLogger(AbstractSite.class);

    private final Member self;
	@NotNull
    protected final AbstractContext context;
	protected AbstractTable table;
	
	public AbstractSite(@NotNull AbstractContext context) {
		this.context = context;
		self = context.getMembership().getSelf();
        export();
	}
	
	/**
	 * FIXME Is it appropriate for {@link #get(Row, Column)} here?
	 */
	@Override
	public ITimestampedCell get(Row r, Column c) {
	    ITimestampedCell tsCell = table.getTimestampedCell(r, c);
	    LOGGER.debug("Site [{}] returns value [{}:{}:{}].", self, r, c, tsCell);

		return tsCell;
	}
	
	/**
	 * FIXME implementation? Is it appropriate for {@link #put(Row, Column, ITimestampedCell)} here?
	 */
	@Override
	public boolean put(Row r, Column c, ITimestampedCell ts_cell) {
		return false;
	}

	@Override
	public void export() {
        RMIUtil.export(this, self.getHost(), self.getPort(), self.getRmiRegistryName());
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
     *
     * FIXME refactor: extract from this class
	 */
    @NotNull
    public static RuntimeMember locateRuntimeMember(@NotNull Member member) {
        Remote obj = RMIUtil.lookup(member.getHost(), member.getRmiRegistryPort(), member.getRmiRegistryName());
        return new RuntimeMember(member, (ISite) obj);
    }

	/**
	 * Locate the stubs for a list of {@link Member}; used later for RMI invocation.
	 * @param members a list of {@link Member}s to be located.
	 * @return 	a list of {@link RuntimeMember}
	 */
    public static List<RuntimeMember> locateRuntimeMembers(@NotNull List<Member> members) {
        return members.stream()
                .map(AbstractSite::locateRuntimeMember)
                .collect(Collectors.toList());
    }

	@NotNull
    @Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(self)
				.toString();
	}

}
