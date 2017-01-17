package client.clientlibrary;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import client.clientlibrary.partitioning.PartitioningTestSuite;
import client.clientlibrary.rvsi.RVSITestSuite;
import client.clientlibrary.transaction.TransactionTestSuite;

/**
 * Test suite for this package {@link client.clientlibrary}.
 * @author hengxin
 * @date Created on Dec 6, 2015
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	PartitioningTestSuite.class,
	RVSITestSuite.class,
	TransactionTestSuite.class
	})
public class ClientLibraryTestSuite {

}
