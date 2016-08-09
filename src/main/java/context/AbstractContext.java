package context;

import org.jetbrains.annotations.NotNull;

import network.membership.IMembership;

/**
 * @author hengxin
 * @date 16-8-9
 */
public abstract class AbstractContext {
    @NotNull protected IMembership membership;

    public IMembership getMembership() { return membership; }
}
