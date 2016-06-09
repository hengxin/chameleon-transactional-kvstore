package network.membership;

import org.intellij.lang.annotations.Language;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.stream.Stream;

import site.ISite;

/**
 * The {@link StaticMembershipFromProperties} loads/maintains a global, static membership view
 * from a properties file.
 *
 * The global membership view consists of a map of {@link ReplicationGroup}s, indexed by their identifiers.
 *
 * @author hengxin
 * @date 16-6-8
 */
public final class StaticMembershipFromProperties implements IMembership {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaticMembershipFromProperties.class);
    @Language("Properties")
    private static final String DEFAULT_PROPERTIES = "network/membership/membership.properties";

    private final Map<Integer, ReplicationGroup> replGrps;

    /**
     * Constructor with default properties file: {@value #DEFAULT_PROPERTIES}.
     */
    public StaticMembershipFromProperties() {
        this(DEFAULT_PROPERTIES);
    }

    /**
     * Constructor with properties file.
     * @param properties  membership properties file
     */
    public StaticMembershipFromProperties(final String properties) {
        replGrps = new MembershipPropertiesParser(properties).parse();
    }

    @Override
    public ISite getMaster(int replGrpId) {
        return replGrps.get(replGrpId).getMaster().getRmiSite();
    }

    @Override
    public Stream<ISite> getSlaves(int replGrpId) {
        return null;
    }
}
