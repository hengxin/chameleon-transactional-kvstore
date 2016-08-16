package context;

import org.jetbrains.annotations.NotNull;

import network.membership.IMembership;
import twopc.timing.CentralizedTimestampOracle;
import twopc.timing.ITimestampOracle;

/**
 * @author hengxin
 * @date 16-8-9
 */
public abstract class AbstractContext {
    @NotNull protected IMembership membership;

    public IMembership getMembership() { return membership; }
    public ITimestampOracle getTsOracle() { return CentralizedTimestampOracle.INSTANCE; }
}
