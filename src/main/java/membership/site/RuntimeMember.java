package membership.site;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import site.ISite;

/**
 * {@link RuntimeMember} represents a remotely-accessed RMI site {@link #rmiSite} in runtime.
 * It also holds a reference {@link #literalMember} of type {@link Member}.
 *
 * @author hengxin
 * @date 16-6-8
 */
public final class RuntimeMember {
    @NotNull private final Member literalMember;
    @Nullable private final ISite rmiSite;

    public RuntimeMember(@NotNull Member literalMember, @Nullable ISite rmiSite) {
        this.literalMember = literalMember;
        this.rmiSite = rmiSite;
    }

    public Member getLiteralMember() { return literalMember; }

    public ISite getRmiSite() { return rmiSite; }
}
