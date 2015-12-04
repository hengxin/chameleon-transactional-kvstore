package network.membership;

import static org.junit.Assert.*;

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
		Member actual_member = Member.parseMember("127.0.0.1:8000;rmi-master:1099");
		
		assertEquals("Fails to parse membership.", expected_member, actual_member);
	}

}
