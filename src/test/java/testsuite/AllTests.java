package testsuite;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import client.clientlibrary.RVSITransactionTest;
import network.membership.StaticMembershipTest;

@RunWith(Suite.class)
@SuiteClasses({RVSITransactionTest.class, StaticMembershipTest.class})
public class AllTests
{
	public static void main(String[] args) throws Exception 
	{                    
       JUnitCore.main("testsuite.AllTests");
	}
}
