package membership.site;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

import util.PropertiesUtil;

import static java.util.stream.Collectors.toMap;

/**
 * Parser of membership properties files.
 *
 * @author hengxin
 * @date 16-6-8
 */
public final class MembershipPropertiesParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(MembershipPropertiesParser.class);
    private static final String SELF = "-1";

    private Properties prop;

    /**
     * Constructor with specified membership properties file.
     *
     * @param properties membership properties file
     * @implNote
     * See <a href = "http://stackoverflow.com/a/2523252/1833118">Post: getSystemResourceAsStream() returns
     * null@stackoverflow</a> for the use of {@code getResourceAsStream()}.
     */
    public MembershipPropertiesParser(@NotNull final String properties) {
        try {
            prop = PropertiesUtil.load(properties);
        } catch (IOException e) {
            LOGGER.error("Failed to load the membership properties file: [{}].", properties);
            System.exit(1);
        }
    }

    @Nullable
    public Member parseSelf() {
        String selfMember = prop.getProperty(SELF);
        if (selfMember != null)
            return Member.parseMember(selfMember).get();
        return null;
    }

    /**
     * Parse {@link #prop} into membership which consists of various {@link ReplicationGroup}s.
     * @return a map of {@link ReplicationGroup}s, indexed by their (integer) identifiers.
     */
    public Map<Integer, ReplicationGroup> parseReplGrps() {
        return prop.stringPropertyNames().stream()
                .filter(idStr -> Integer.parseInt(idStr) != -1)
                .map(idStr -> ReplicationGroup.parseReplicationGroup(Integer.parseInt(idStr), prop.getProperty(idStr)))
                .collect(toMap(ReplicationGroup::getReplGrpId,
                        Function.identity()));
    }

}
