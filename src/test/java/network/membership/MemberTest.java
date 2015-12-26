package network.membership;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

public class MemberTest
{
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testParseMember()
	{
		Member expected_member = new Member("127.0.0.1", 8000, "rmi-master", 1099);
		Member actual_member = Member.parseMember("127.0.0.1@8000;rmi-master@1099").get();
		
		assertEquals("Fails to parse membership.", expected_member, actual_member);
	}
	
	@Test
	public void testParseMembers()
	{
		String members = "127.0.0.1@8000;rmi-master@1099, 127.0.0.1@6000;rmi-slave@1099";
		Member m1 = new Member("127.0.0.1", 8000, "rmi-master", 1099);
		Member m2 = new Member("127.0.0.1", 6000, "rmi-slave", 1099);

		List<Member> expected_member_list = new ArrayList<>();
		expected_member_list.add(m1);
		expected_member_list.add(m2);

		List<Member> actual_member_list = Member.parseMembers(members);

		assertTrue("Fails to parse several members.", CollectionUtils.isEqualCollection(expected_member_list, actual_member_list));
	}

}
