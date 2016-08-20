package context;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import membership.coordinator.ICoordinatorMembership;
import membership.site.ISiteMembership;
import twopc.timing.CentralizedTimestampOracle;
import twopc.timing.ITimestampOracle;

/**
 * @author hengxin
 * @date 16-8-9
 */
public abstract class AbstractContext implements Serializable {
    protected @NotNull ISiteMembership membership;
    protected ICoordinatorMembership coordMembership;

    public @NotNull ISiteMembership getMembership() { return membership; }
    public ITimestampOracle getTsOracle() { return CentralizedTimestampOracle.INSTANCE; }  // FIXME refactor it
}
