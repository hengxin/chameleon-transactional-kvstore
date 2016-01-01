package network.membership;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.google.common.base.MoreObjects;

import exception.network.membership.MemberParseException;

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
	 * 
	 * @param		member String format of {@link Member}
	 * @return		A {@link Member} instance
	 * @throws MemberParseException		if it fails to parse this member
	 */
	public static final Member parseMember(String member) throws MemberParseException {
		String[] parts = member.replaceAll("\\s", "").split("@|;");
		
		try
		{
			final String addr_ip = parts[0];
			final int addr_port = Integer.parseInt(parts[1]);
			final String rmi_registry_name = parts[2];
			final int rmi_registry_port = Integer.parseInt(parts[3]);

			return new Member(addr_ip, addr_port, rmi_registry_name, rmi_registry_port);
		} catch (NullPointerException | NumberFormatException e)	// FIXME catch NullPointerException or not?
		{
			throw new MemberParseException(String.format("Failed to parse [%s] to a Member because it is ill-formated.", member), e.getCause());
		}
	}
	
	/**
	 * Parse a collection of strings, separated by commas, into {@link Member} instances.
	 * 
	 * @param members String format of a list of {@link Member}
	 * @return A list of {@link Member}; it may be empty.
	 * @throws MemberParseException		if it fails to parse some members
	 */
	public static final List<Member> parseMembers(String members) throws MemberParseException {
		String[] member_array = members.replaceAll("\\s", "").split(",");

		return Arrays.stream(member_array).parallel() 	// TODO choose appropriate collector for parallel stream
				.map(Member::parseMember)
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
