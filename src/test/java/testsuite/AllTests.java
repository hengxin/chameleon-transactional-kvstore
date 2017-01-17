package testsuite;

import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import client.clientlibrary.ClientLibraryTestSuite;
import kvs.table.KVSTableTestSuite;
import membership.NetworkTestSuite;

@RunWith(Suite.class)
@SuiteClasses({ClientLibraryTestSuite.class, KVSTableTestSuite.class, NetworkTestSuite.class})
public class AllTests {
	public static void main(String[] args) throws Exception {
       JUnitCore.main("testsuite.AllTests");
	}
}
