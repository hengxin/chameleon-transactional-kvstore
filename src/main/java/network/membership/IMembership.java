package network.membership;

import java.util.List;
import java.util.stream.Stream;

import site.ISite;

/**
 * Interface {@link IMembership} provides accesses to master/slave sites.
 * @author hengxin
 * @date 16-6-8
 */
public interface IMembership {
    int getReplGrpNo();
    ISite getMaster(int replGrpId);
    List<ISite> getMasterSites();
    List<Member> getMasterMembers();
    Stream<ISite> getSlaves(int replGrpId);
    ReplicationGroup getReplGrp(int index);

    Member getSelf();
}
