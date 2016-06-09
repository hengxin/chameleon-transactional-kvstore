package network.membership;

import site.ISite;

/**
 * {@link RuntimeMember} represents a remotely-accessed RMI site {@link #rmiSite} in runtime.
 * It also holds a reference {@link #literalMember} of type {@link Member}.
 *
 * @author hengxin
 * @date 16-6-8
 */
public final class RuntimeMember {
    private final Member literalMember;
    private final ISite rmiSite;

    public RuntimeMember(Member literalMember, ISite rmiSite) {
        this.literalMember = literalMember;
        this.rmiSite = rmiSite;
    }

    public ISite getRmiSite() {
        return rmiSite;
    }
}
