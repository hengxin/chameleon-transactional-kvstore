package client.clientlibrary.partitioning;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for this package {@link client.clientlibrary.partitioning}.
 * @author hengxin
 * @date Created on Jan 6, 2016
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	ConsistentHashingDynamicPartitionerTest.class 
	})
public class PartitioningTestSuite {

}
