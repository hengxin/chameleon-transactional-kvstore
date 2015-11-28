package client.clientlibrary.rvsi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecificationTest;
import client.clientlibrary.rvsi.rvsispec.BVSpecificationTest;
import client.clientlibrary.rvsi.vc.AbstractVersionConstraintTest;

@RunWith(Suite.class)
@SuiteClasses({
	AbstractRVSISpecificationTest.class,
	BVSpecificationTest.class,
	AbstractVersionConstraintTest.class
})
public class RVSITestSuite
{

}
