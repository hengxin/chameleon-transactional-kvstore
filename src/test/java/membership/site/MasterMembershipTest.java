package membership.site;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @author hengxin
 * @date Created on 12-04-2015
 */
public class MasterMembershipTest
{
	private final static String FILE = "./master/membership-master-test.properties";

	private AbstractStaticMembership master_membership;
	
	@Before
	public void setUp() throws Exception
	{
		this.master_membership = new MasterMembership(FILE);
	}

	@Test
	public void testLoadMembershipFromProp()
	{
		Member actual_master = ((MasterMembership) this.master_membership).self();
		List<Member> actual_slaves = ((MasterMembership) this.master_membership).getSlaves();

		Member expected_master = Member.parseMember("127.0.0.1@8000;master0@1099").get();
		Member expected_slave1 = Member.parseMember("127.0.0.1@5000;slave01@1099").get();
		Member expected_slave2 = Member.parseMember("127.0.0.1@6000;slave02@1099").get();
		List<Member> expected_slaves = new ArrayList<>();
		expected_slaves.add(expected_slave1);
		expected_slaves.add(expected_slave2);
		
		assertEquals("Fails to load the master which should be " + expected_master, expected_master, actual_master);
		assertTrue("Fails to load the slaves.", CollectionUtils.isEqualCollection(expected_slaves, actual_slaves));
	}

}
