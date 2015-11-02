/**
 * 
 */
package network.membership;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

/**
 * @author hengxin
 * @date Created on 11-02-2015
 * 
 * Test the class {@link StaticMembership}
 */
public class StaticMembershipTest
{

	private static final String MEMBERSHIP_TEST_PROPERTIES_FILE = "membershiptest.properties";

	/**
	 * Test method for {@link network.membership.StaticMembership#extractAddrs()}.
	 * using the src/test/resources/membershiptest.properties file
	 */
	@Test
	public void testExtractAddrs()
	{
		Properties prop = new Properties();
		StaticMembership.load(prop, StaticMembershipTest.MEMBERSHIP_TEST_PROPERTIES_FILE);
		
		String[] addrs = StaticMembership.extractAddrs(prop);
		assertArrayEquals("There are wrong members.", new String[] {"192.168.231.128", "0.0.0.0", "0.0.0.0"}, addrs);
	}

	/**
	 * Test method for {@link network.membership.StaticMembership#load(java.lang.String)}.
	 * test using the src/test/resources/membershiptest.properties file
	 */
	@Test
	public void testLoad()
	{
		Properties prop = new Properties();
		StaticMembership.load(prop, StaticMembershipTest.MEMBERSHIP_TEST_PROPERTIES_FILE);
		assertEquals("There should be 3 members.", 3, prop.size());
	}

}
