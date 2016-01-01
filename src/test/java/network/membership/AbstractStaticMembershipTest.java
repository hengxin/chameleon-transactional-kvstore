package network.membership;

import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import exception.network.membership.MemberParseException;

/**
 * Test {@link AbstractStaticMembership#loadProp()}.
 * @author hengxin
 * @date Created on Jan 1, 2016
 */
@Ignore
public final class AbstractStaticMembershipTest {

	private final static String FILE = "membership-abstract-test.properties";

	AbstractStaticMembership membership; 
	
	@Before
	public void setUp() throws Exception {
		this.membership = new BasicMembership(FILE);
	}

	@Test
	public void testLoadProp() {
		Properties expected_prop = new Properties();
		expected_prop.setProperty("master", "192.168.107.128@8000;master@1099");
		expected_prop.setProperty("slave", "192.168.107.128@5000;slave@1099");
		
		Properties actual_prop = membership.prop;
		
		assertEquals("Fails to load the properties file: " + FILE, expected_prop, actual_prop);
	}
	
	private final class BasicMembership extends AbstractStaticMembership {

		public BasicMembership(String file) throws MemberParseException {
			super(file);
		}

		@Override
		public void parseMembershipFromProp() {
		}
		
	}

}
