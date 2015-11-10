package kvs.table;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MultiTimestampedCellsStoreTest.class, SingleTimestampedCellStoreTest.class, TimestampedCellTest.class})
public class KVSTableTestSuite
{

}
