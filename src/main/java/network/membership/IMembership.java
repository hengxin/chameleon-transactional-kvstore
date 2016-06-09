package network.membership;

import java.util.stream.Stream;

import site.ISite;

/**
 * Interface {@link IMembership} provides accesses to master/slave sites.
 * @author hengxin
 * @date 16-6-8
 */
public interface IMembership {
    ISite getMaster(int replGrpId);
    Stream<ISite> getSlaves(int replGrpId);
}
