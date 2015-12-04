package master.communication;

import java.util.List;

import network.membership.MasterMembership;
import network.membership.Member;
import slave.ISlave;

/**
 * @author hengxin
 * @date Created on 12-04-2015
 */
public enum MasterContact
{
	INSTANCE;
	
	private final static String MASTER_MEMBERSHIP_PROPERTIES_FILE = "./master/membership-master.properties";
	
	private final MasterMembership master_membership = new MasterMembership(MASTER_MEMBERSHIP_PROPERTIES_FILE);
	private final List<ISlave> slave_stubs;
	
	private MasterContact()
	{
		this.master_membership.loadMembership();
		this.slave_stubs = Member.parseStubs(this.master_membership.getSlaves());
	}
	
	public String getMasterAddrIp()
	{
		return this.master_membership.getSelf().getAddrIp();
	}
	
	public int getMasterRMIRegistryPort()
	{
		return this.master_membership.getSelf().getRmiRegistryPort();
	}
	
	public String getMasterRMIRegistryName()
	{
		return this.master_membership.getSelf().getRmiRegistryName();
	}
}
