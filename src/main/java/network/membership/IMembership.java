package network.membership;

/**
 * Group membership service
 * 
 * @author hengxin
 * @date Created on 11-02-2015
 */
public interface IMembership
{
	/**
	 * obtain the array of member addresses
	 * @return array of addresses (hostname or ip)
	 */
	public String[] getMemberAddrs();

	/**
	 * @return the address of the master
	 */
	public String getMasterAddr();
}
