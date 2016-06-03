package kvs;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import kvs.component.OrdinalTest;
import kvs.compound.CKeyToOrdinalIndexTest;
import kvs.compound.TimestampedCellTest;

/**
 * KVS database related test cases.
 * @author hengxin
 * @date Created on Dec 12, 2015
 */
@RunWith(Suite.class)
@SuiteClasses({OrdinalTest.class,
	CKeyToOrdinalIndexTest.class,
	TimestampedCellTest.class})
public class KVSTestSuite {

}
