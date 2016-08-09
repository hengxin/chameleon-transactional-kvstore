package network;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import network.membership.MasterMembershipTest;
import network.membership.MemberTest;

@RunWith(Suite.class)
@SuiteClasses({
	MemberTest.class,
	MasterMembershipTest.class
	})
public class NetworkTestSuite {

}
