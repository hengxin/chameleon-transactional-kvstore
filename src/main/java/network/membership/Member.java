package network.membership;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
	 * Parse a string of the format of "addr_ip:addr_port;rmi_registry_name:rmi_registry_port"
	 * into a {@link Member} instance.
	 * 
	 * @param member String format of {@link Member}
	 * @return A {@link Member} instance; possibly {@code null} if {#param member} is ill-formated.
	 */
	public static final Member parseMember(String member)
	{
		String[] parts = member.split(":|;");
		
		String addr_ip;
		int addr_port;
		String rmi_registry_name;
		int rmi_registry_port;

		try
		{
			addr_ip = parts[0];
			addr_port = Integer.parseInt(parts[1]);
			rmi_registry_name = parts[2];
			rmi_registry_port = Integer.parseInt(parts[3]);

			return new Member(addr_ip, addr_port, rmi_registry_name, rmi_registry_port);
		} catch (NullPointerException npe)
		{
			LOGGER.error("This member String ({}) is ill-formated. The details are: {}.", member, npe);
			return null;
		} catch (NumberFormatException nfe)
		{
			LOGGER.error("This member String ({}) is ill-formated. The details are: {}.", member, nfe);
			return null;
		}
	}
	
	/**
	 * Parse a list of {@link Member} strings, separated by commas.
	 * 
	 * @param members String format of a list of {@link Member}
	 * @return A list of {@link Member}
	 */
	public static final List<Member> parseMembers(String members)
	{
		String[] slave_array = members.split(",");
		return Arrays.stream(slave_array).map(slave -> Member.parseMember(slave)).collect(Collectors.toList());
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
