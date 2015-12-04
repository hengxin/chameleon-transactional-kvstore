package network;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import network.membership.AbstractStaticMembershipTest;
import network.membership.MemberTest;
import network.membership.StaticMembershipTest;

@RunWith(Suite.class)
@SuiteClasses({
	StaticMembershipTest.class,
	MemberTest.class,
	AbstractStaticMembershipTest.class
	})
public class NetworkTestSuite
{

}
