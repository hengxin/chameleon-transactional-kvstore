package context;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import membership.coordinator.ICoordinatorMembership;
import membership.site.ISiteMembership;
import timing.ITimestampOracle;

/**
 * @author hengxin
 * @date 16-8-9
 */
public abstract class AbstractContext implements Serializable {
    private static final long serialVersionUID = 6213467098181726391L;

    @NotNull protected ISiteMembership membership;
    protected ICoordinatorMembership coordMembership;
    @NotNull protected ITimestampOracle to;

    @NotNull
    public ISiteMembership getMembership() { return membership; }
    @NotNull
    public ITimestampOracle getTsOracle() { return to; }

}
