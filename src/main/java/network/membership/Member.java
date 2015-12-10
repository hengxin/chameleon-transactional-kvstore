package network.membership;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.MoreObjects;

import site.ISite;

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
		} catch (NullPointerException | NumberFormatException e)
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

	/**
	 * Locate the stub for the {@link Member}; Used later for RMI.
	 * 
	 * @param member An {@link Member} representing a site
	 * @return 
	 * 		A stub for a remote object, wrapped by {@link Optional}; 
	 * 		may be {@code Optional.empty()} if it fails to parse a stub from @param member.
	 */
	public static Optional<ISite> parseStub(Member member)
	{
			try
			{
				return Optional.of((ISite) LocateRegistry.getRegistry(member.getAddrIp()).lookup(member.getRmiRegistryName()));
			} catch (RemoteException | NotBoundException e)
			{
				Throwable cause = e.getCause();
				LOGGER.warn("Failed to locate the remote stub for {}. I will ignore it for now. \n {}", member, 
						Objects.isNull(cause) ? "Causes Unknown." : cause.toString());
				return Optional.empty();
			}
	}
	
	/**
	 * Locate the stubs for a list of {@link Member}s.
	 * The remote stubs which cannot be located are ignored.
	 * 
	 * @param members 
	 * 		A list of {@link Member}s to be parsed.
	 * @return 
	 * 		A list of {@link ISite} stubs; 
	 * 		the list may be empty if none of the {@link Member}s is parsed successfully.
	 * 
	 * @implNote
	 * 		This code using {@link Optional} to avoid null-check is due to
	 * 		<a href = "http://stackoverflow.com/a/34170759/1833118">Brian Goetz @ Stackoverflow</a>.
	 */
	public static List<ISite> parseStubs(List<Member> members)
	{
		return members.stream()
				.map(Member::parseStub)
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
