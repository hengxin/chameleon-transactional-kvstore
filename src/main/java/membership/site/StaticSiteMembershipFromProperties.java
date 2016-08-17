package membership.site;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import site.ISite;

/**
 * The {@link StaticSiteMembershipFromProperties} loads/maintains a global, static membership view
 * from a properties file.
 *
 * The global membership view consists of a map of {@link ReplicationGroup}s, indexed by their identifiers.
 *
 * @author hengxin
 * @date 2016-6-8
 */
public final class StaticSiteMembershipFromProperties implements ISiteMembership {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaticSiteMembershipFromProperties.class);
    @Language("Properties")
    private static final String DEFAULT_PROPERTIES = "network/membership/membership.properties";

    @NotNull private final Member self;
    @NotNull private final Map<Integer, ReplicationGroup> replGrps;

    /**
     * Constructor with default properties file: {@value #DEFAULT_PROPERTIES}.
     */
    public StaticSiteMembershipFromProperties() {
        this(DEFAULT_PROPERTIES);
    }

    /**
     * Constructor with properties file.
     * @param properties  membership properties file
     */
    public StaticSiteMembershipFromProperties(final String properties) {
        MembershipPropertiesParser parser = new MembershipPropertiesParser(properties);
        self = parser.parseSelf();
        replGrps = parser.parseReplGrps();
    }

    @Override
    public ReplicationGroup getReplGrp(final int replGrpId) {
        return replGrps.get(replGrpId);
    }

    @Override
    public Member getSelf() {
        return self;
    }

    @Override
    public int getReplGrpNo() { return replGrps.size(); }

    @Override
    public ISite getMaster(int replGrpId) { return replGrps.get(replGrpId).getMaster().getRmiSite(); }

    @Override
    public List<ISite> getMasterSites() {
        return replGrps.values().stream()
                .map(ReplicationGroup::getMasterSite)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> getMasterMembers() {
        return replGrps.values().stream()
                .map(ReplicationGroup::getMasterMember)
                .collect(Collectors.toList());
    }

    @Override
    public Stream<ISite> getSlaves(int replGrpId) {
        return replGrps.get(replGrpId)
                .getSlaves().stream()
                .map(RuntimeMember::getRmiSite);
    }

}
