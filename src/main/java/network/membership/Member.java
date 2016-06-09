package network.membership;

import com.google.common.base.MoreObjects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A member (i.e., site), as a communication entity,
 * holds four (self-descriptive) attributes:
 * <p>
 * <ul>
 * <li> {@link #host}
 * <li> {@link #port}
 * 	(reserved; not used because RMI and JMS will choose their own ports.)
 * <li> {@link #rmiRegistryName}
 * <li> {@link #rmiRegistryPort}
 * </ul>
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class Member {
	private static final Logger LOGGER = LoggerFactory.getLogger(Member.class);

	private final String host;
	private final int port;
	private final String rmiRegistryName;
	private final int rmiRegistryPort;

	public Member(final String host, final int port,
                  final String rmiRegistryName, final int rmiRegistryPort) {
		this.host = host;
		this.port = port;
		this.rmiRegistryName = rmiRegistryName;
		this.rmiRegistryPort = rmiRegistryPort;
	}

	/**
	 * Parse a string of the format of "host@port;rmiRegistryName@rmiRegistryPort"
	 * into a {@link Member} instance.
	 * @param		member String format of {@link Member}
	 * @return		an {@link Optional}-wrapped {@link Member} instance; it may be {@code Optional.empty()}
	 * 	if an error occurs during parse.
	 */
	public static Optional<Member> parseMember(String member) {
		String[] parts = member.replaceAll("\\s", "").split("@|;");		// FIXME remove hard-wired code here
		
		try {
			final String addr_ip = parts[0];
			final int addr_port = Integer.parseInt(parts[1]);
			final String rmi_registry_name = parts[2];
			final int rmi_registry_port = Integer.parseInt(parts[3]);

			return Optional.of(new Member(addr_ip, addr_port, rmi_registry_name, rmi_registry_port));
		} catch (NullPointerException | NumberFormatException e) {	// FIXME catch NullPointerException or not?
			LOGGER.error("Failed to parse [{}] to a Member because it is ill-formated. \\n {}", member, e.getCause());
			return Optional.empty();
		}
	}
	
	/**
	 * Parse a collection of strings, separated by commas, into {@link Member} instances.
	 * 
	 * @param members String format of a list of {@link Member}
	 * @return a list of {@link Member}; Note that only the {@link Member}s that are successfully
	 * 	parsed are returned; therefore, the list may be empty.
	 */
	public static List<Member> parseMembers(String members) {
		return Pattern.compile(",").splitAsStream(members.replaceAll("\\s", ""))
								   .map(Member::parseMember)
								   .filter(Optional::isPresent)
								   .map(Optional::get)
								   .collect(Collectors.toList());
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getRmiRegistryName() {
		return rmiRegistryName;
	}

	public int getRmiRegistryPort() {
		return rmiRegistryPort;
	}


	@Override
	public int hashCode() {
		return Objects.hash(host, port, rmiRegistryName, rmiRegistryPort);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null || o.getClass() != this.getClass())
			return false;

		Member that = (Member) o;
		
		return Objects.equals(host, that.host)
				&& Objects.equals(port, that.port)
				&& Objects.equals(rmiRegistryName, that.rmiRegistryName)
				&& Objects.equals(rmiRegistryPort, that.rmiRegistryPort);
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("host", this.host)
				.add("port", this.port)
				.add("rmiRegistryName", this.rmiRegistryName)
				.add("rmiRegistryPort", this.rmiRegistryPort)
				.toString();
	}

}
