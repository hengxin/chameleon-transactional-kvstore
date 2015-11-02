/**
 * 
 */
package network.membership;

/**
 * @author hengxin
 * @date Created on 02-11-2015
 * 
 * Group membership service
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
