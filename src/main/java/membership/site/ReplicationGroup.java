package membership.site;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import site.AbstractSite;
import site.ISite;

/**
 * A {@link ReplicationGroup}, associated with a globally unique integer identifier ({@link #replGrpId}),
 * consists of a <i>master</i> {@link RuntimeMember} and a collection of <i>slave</i> {@link RuntimeMember}s.
 *
 * @author hengxin
 * @date 16-6-8
 */
public final class ReplicationGroup implements Serializable {
    private final int replGrpId;
    private RuntimeMember master;
    private final List<RuntimeMember> slaves;

    public ReplicationGroup(int replGrpId, RuntimeMember master, List<RuntimeMember> slaves) {
        this.replGrpId = replGrpId;
        this.master = master;
        this.slaves = slaves;
    }

    public int getReplGrpId() { return replGrpId; }

    public RuntimeMember getMaster() { return master; }
    public Member getMasterMember() { return master.getLiteralMember(); }
    @Nullable
    public ISite getMasterSite() {
        if (master.getRmiSite() == null)
            master = AbstractSite.locateRuntimeMember(master.getLiteralMember());
        return master.getRmiSite();
    }

    public List<RuntimeMember> getSlaves() { return slaves; }

    /**
     * Parse the {@link ReplicationGroup} whose id is <code>idStr</code>.
     * @param grpId the id of the {@link ReplicationGroup} to be parsed
     * @param grpStr the string format of {@link ReplicationGroup} to be parsed
     * @return  an instance of {@link ReplicationGroup}
     */
    @NotNull
    public static ReplicationGroup parseReplicationGroup(final int grpId, @NotNull final String grpStr) {
        int sep = grpStr.indexOf(',');
        String masterStr = grpStr.substring(0, sep);
        String slavesStr = grpStr.substring(sep + 1).trim();

        Member master = Member.parseMember(masterStr).get();
        List<Member> slaves = Member.parseMembers(slavesStr);

        return new ReplicationGroup(grpId,
                AbstractSite.locateRuntimeMember(master),
                AbstractSite.locateRuntimeMembers(slaves));
    }

    /**
     * Return a site for read.
     * It prefers a slave site.
     * If no slaves are available, it returns the master.
     *
     * @return	an {@link ISite} in this {@link ReplicationGroup}
     */
    @Nullable
    public ISite getSiteForRead() {
        return slaves.isEmpty() ? master.getRmiSite() : getRandomSlave();
    }

    /**
     * @return	a random slave site in this {@link ReplicationGroup}
     */
    @Nullable
    private ISite getRandomSlave() {
        return slaves.get(new Random().nextInt(slaves.size()))
                .getRmiSite();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ReplicationGroupId", replGrpId)
                .addValue(master)
                .addValue(slaves)
                .toString();
    }
}
