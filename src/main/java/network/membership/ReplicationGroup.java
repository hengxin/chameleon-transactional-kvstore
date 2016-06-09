package network.membership;

import java.util.List;

/**
 * A {@link ReplicationGroup}, associated with a globally unique integer identifier ({@link #replGrpId}),
 * consists of a <i>master</i> {@link RuntimeMember} and a collection of <i>slave</i> {@link RuntimeMember}s.
 *
 * @author hengxin
 * @date 16-6-8
 */
public final class ReplicationGroup {
    private final int replGrpId;
    private final RuntimeMember master;
    private final List<RuntimeMember> slaves;

    public ReplicationGroup(int replGrpId, RuntimeMember master, List<RuntimeMember> slaves) {
        this.replGrpId = replGrpId;
        this.master = master;
        this.slaves = slaves;
    }

    public int getReplGrpId() {
        return replGrpId;
    }

    public RuntimeMember getMaster() {
        return master;
    }
}
