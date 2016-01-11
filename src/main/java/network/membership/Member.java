package network.membership;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

/**
 * A member (i.e., site), as a communication entity,
 * holds four (self-descriptive) attributes:
 * <p>
 * <ul>
 * <li> {@link #addr_ip}
 * <li> {@link #addr_port} 
 * 	(reserved; not used because RMI and JMS will choose their own ports.)
 * <li> {@link #rmi_registry_name}
 * <li> {@link #rmi_registry_port}
 * </ul>
 * @author hengxin
 * @date Created on 12-03-2015
 */
public final class Member {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Member.class);

	private final String addr_ip;
	private final int addr_port;
	private final String rmi_registry_name;
	private final int rmi_registry_port; 

	public Member(final String addr_ip, final int addr_port, final String rmi_registry_name, final int rmi_registry_port) {
		this.addr_ip = addr_ip;
		this.addr_port = addr_port;
		this.rmi_registry_name = rmi_registry_name;
		this.rmi_registry_port = rmi_registry_port;
	}

	/**
	 * Parse a string of the format of "addr_ip@addr_port;rmi_registry_name@rmi_registry_port"
	 * into a {@link Member} instance.
	 * @param		member String format of {@link Member}
	 * @return		an {@link Optional}-wrapped {@link Member} instance; it may be {@code Optional.empty()}
	 * 	if an error occurs during parse.
	 */
	public static final Optional<Member> parseMember(String member) {
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
	public static final List<Member> parseMembers(String members) {
		return Pattern.compile(",").splitAsStream(members.replaceAll("\\s", ""))
								   .map(Member::parseMember)
								   .filter(Optional::isPresent)
								   .map(Optional::get)
								   .collect(Collectors.toList());
	}

	public String getAddrIp() {
		return addr_ip;
	}

	public int getAddrPort() {
		return addr_port;
	}

	public String getRmiRegistryName() {
		return rmi_registry_name;
	}

	public int getRmiRegistryPort() {
		return rmi_registry_port;
	}


	@Override
	public int hashCode() {
		return Objects.hash(addr_ip, addr_port, rmi_registry_name, rmi_registry_port);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(!(o.getClass() == this.getClass()))
			return false;
		
		Member that = (Member) o;
		
		return Objects.equals(this.addr_ip, that.addr_ip) 
				&& Objects.equals(this.addr_port, that.addr_port)
				&& Objects.equals(this.rmi_registry_name, that.rmi_registry_name) 
				&& Objects.equals(this.rmi_registry_port, that.rmi_registry_port);
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("addr_ip", this.addr_ip)
				.add("addr_port", this.addr_port)
				.add("rmi_registry_name", this.rmi_registry_name)
				.add("rmi_registry_port", this.rmi_registry_port)
				.toString();
	}

}
