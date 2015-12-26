package kvs.table;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import kvs.compound.TimestampedCellTest;

@RunWith(Suite.class)
@SuiteClasses({ MultiTimestampedCellsStoreTest.class, SingleTimestampedCellStoreTest.class, TimestampedCellTest.class, TableTest.class})
public class KVSTableTestSuite
{

}
