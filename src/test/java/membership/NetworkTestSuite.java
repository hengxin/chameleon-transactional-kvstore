package membership;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import membership.site.MasterMembershipTest;
import membership.site.MemberTest;

@RunWith(Suite.class)
@SuiteClasses({
	MemberTest.class,
	MasterMembershipTest.class
	})
public class NetworkTestSuite {

}
