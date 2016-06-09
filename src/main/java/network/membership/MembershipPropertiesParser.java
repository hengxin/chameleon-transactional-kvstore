package network.membership;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import site.AbstractSite;
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

    private Properties prop;

    /**
     * Constructor with specified membership properties file.
     *
     * @param properties membership properties file
     * @implNote
     * See <a href = "http://stackoverflow.com/a/2523252/1833118">Post: getSystemResourceAsStream() returns
     * null@stackoverflow</a> for the use of {@code getResourceAsStream()}.
     */
    public MembershipPropertiesParser(final String properties) {
        try {
            prop = PropertiesUtil.load(properties);
        } catch (IOException e) {
            LOGGER.error("Failed to load the membership properties file: [{}].", properties);
            System.exit(1);
        }
    }

    /**
     * Parse {@link #prop} into membership which consists of various {@link ReplicationGroup}s.
     * @return a map of {@link ReplicationGroup}s, indexed by their (integer) identifiers.
     */
    public Map<Integer, ReplicationGroup> parse() {
        return prop.stringPropertyNames().stream()
                .map(this::parseReplicationGroup)
                .collect(toMap(ReplicationGroup::getReplGrpId,
                        Function.identity()));
    }

    /**
     * Parse the {@link ReplicationGroup} whose id is <code>idStr</code>.
     * @param idStr the id of the {@link ReplicationGroup} to be parsed
     * @return  an instance of {@link ReplicationGroup}
     */
    private ReplicationGroup parseReplicationGroup(final String idStr) {
        String grpStr = prop.getProperty(idStr);

        int sep = grpStr.indexOf(',');
        String masterStr = grpStr.substring(0, sep);
        String slavesStr = grpStr.substring(sep + 1).trim();

        Member master = parseMember(masterStr).get();
        List<Member> slaves = parseMembers(slavesStr);

        return new ReplicationGroup(Integer.parseInt(idStr),
                AbstractSite.locateRuntimeMember(master).get(),
                AbstractSite.locateRuntimeMembers(slaves));
    }

    /**
     * Parse a string of the format of "addr_ip@addr_port;rmi_registry_name@rmi_registry_port"
     * into an instance of {@link Member}.
     *
     * @param	memberStr	String format of {@link Member}
     * @return	an {@link Optional}-wrapped {@link Member} instance; it may be {@code Optional.empty()}
     * 	if an error occurs.
     */
    private Optional<Member> parseMember(final String memberStr) {
        try {
            String[] parts = memberStr.replaceAll("\\s", "").split("@|;");		// FIXME remove hard-wired code here

            String ip = parts[0];
            int port = Integer.parseInt(parts[1]);
            String rmiRegistryName = parts[2];
            int rmiRegistryPort = Integer.parseInt(parts[3]);

            return Optional.of(new Member(ip, port, rmiRegistryName, rmiRegistryPort));
        } catch (PatternSyntaxException | NumberFormatException e) {
            LOGGER.error("Failed to parse [{}] into a Member.", memberStr, e.getCause());
            return Optional.empty();
        }
    }

    /**
     * Parse a collection of strings, separated by commas, into {@link Member} instances.
     *
     * @param membersStr String format of a list of {@link Member}
     * @return a list of {@link Member}
     * @implNote
     *  Only the {@link Member}s that are successfully parsed are returned;
     *  therefore, the returned list may be empty.
     */
    private List<Member> parseMembers(final String membersStr) {
        return Pattern.compile(",").splitAsStream(membersStr.replaceAll("\\s", ""))
                .map(this::parseMember)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

}
