package network;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import network.membership.StaticMembershipTest;

@RunWith(Suite.class)
@SuiteClasses({StaticMembershipTest.class})
public class NetworkTestSuite
{

}
