package slave.communication;

import master.IMaster;
import network.membership.Member;
import network.membership.SlaveMembership;

/**
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class SlaveContact
{
	private final static String SLAVE_MEMBERSHIP_PROPERTIES_FILE = "./slave/membership-slave.properties";
	private final SlaveMembership slave_membership = new SlaveMembership(SLAVE_MEMBERSHIP_PROPERTIES_FILE);
	
	private final IMaster master_stub;
	
	private SlaveContact()
	{
		this.master_stub = (IMaster) Member.parseStub(this.slave_membership.getMaster());	// handle {@code null}
	}
}
