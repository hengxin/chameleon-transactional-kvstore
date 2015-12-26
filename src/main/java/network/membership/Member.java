package network.membership;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
public final class Member
{
	private final static Logger LOGGER = LoggerFactory.getLogger(Member.class);
	
	private final String addr_ip;
	private final int addr_port;
	private final String rmi_registry_name;
	private final int rmi_registry_port; 

	public Member(final String addr_ip, final int addr_port, final String rmi_registry_name, final int rmi_registry_port)
	{
		this.addr_ip = addr_ip;
		this.addr_port = addr_port;
		this.rmi_registry_name = rmi_registry_name;
		this.rmi_registry_port = rmi_registry_port;
	}

	/**
	 * Parse a string of the format of "addr_ip@addr_port;rmi_registry_name@rmi_registry_port"
	 * into a {@link Member} instance.
	 * 
	 * @param member String format of {@link Member}
	 * @return 
	 * 		A {@link Member} instance; possibly {@code null} if {#param member} is ill-formated.
	 * 		The return value is wrapped by {@link Optional} of Java 8.
	 */
	public static final Optional<Member> parseMember(String member)
	{
		String[] parts = member.replaceAll("\\s", "").split("@|;");
		
		try
		{
			final String addr_ip = parts[0];
			final int addr_port = Integer.parseInt(parts[1]);
			final String rmi_registry_name = parts[2];
			final int rmi_registry_port = Integer.parseInt(parts[3]);

			return Optional.of(new Member(addr_ip, addr_port, rmi_registry_name, rmi_registry_port));
		} catch (NullPointerException | NumberFormatException e)	// FIXME catch NullPointerException or not?
		{
			Throwable cause = e.getCause();
			LOGGER.warn("Failed to parse {} because it is ill-formated. I will ignore it for now. \n {}", member, 
					Objects.isNull(cause) ? "Causes Unknown." : cause.toString());
			return Optional.empty();
		}
	}
	
	/**
	 * Parse a list of {@link Member} strings, separated by commas.
	 * 
	 * @param members String format of a list of {@link Member}
	 * @return A list of {@link Member}; it may be empty.
	 * 
	 * @implNote
	 * 		This code using {@link Optional} to avoid null-check is due to
	 * 		<a href = "http://stackoverflow.com/a/34170759/1833118">Brian Goetz @ Stackoverflow</a>.
	 */
	public static final List<Member> parseMembers(String members)
	{
		String[] member_array = members.replaceAll("\\s", "").split(",");

		return Arrays.stream(member_array) 
				.map(Member::parseMember)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
	}

	public String getAddrIp()
	{
		return addr_ip;
	}

	public int getAddrPort()
	{
		return addr_port;
	}

	public String getRmiRegistryName()
	{
		return rmi_registry_name;
	}

	public int getRmiRegistryPort()
	{
		return rmi_registry_port;
	}


	@Override
	public int hashCode()
	{
		return Objects.hash(addr_ip, addr_port, rmi_registry_name, rmi_registry_port);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(!(o.getClass() == this.getClass()))
			return false;
		
		Member that = (Member) o;
		
		return Objects.equals(this.addr_ip, that.addr_ip) && Objects.equals(this.addr_port, that.addr_port)
				&& Objects.equals(this.rmi_registry_name, that.rmi_registry_name) && Objects.equals(this.rmi_registry_port, that.rmi_registry_port);
	}
	
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("addr_ip", this.addr_ip)
				.add("addr_port", this.addr_port)
				.add("rmi_registry_name", this.rmi_registry_name)
				.add("rmi_registry_port", this.rmi_registry_port)
				.toString();
	}
}
