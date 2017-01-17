package membership.site;

import java.util.List;
import java.util.stream.Stream;

import site.ISite;

/**
 * Interface {@link ISiteMembership} provides accesses to master/slave sites.
 * @author hengxin
 * @date 16-6-8
 */
public interface ISiteMembership {
    int getReplGrpNo();
    ISite getMaster(int replGrpId);
    List<ISite> getMasterSites();
    List<Member> getMasterMembers();
    Stream<ISite> getSlaves(int replGrpId);
    ReplicationGroup getReplGrp(int index);

    Member getSelf();
}
