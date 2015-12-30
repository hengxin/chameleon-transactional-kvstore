package client.clientlibrary.rvsi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecificationTest;
import client.clientlibrary.rvsi.rvsispec.BVSpecificationTest;
import client.clientlibrary.rvsi.rvsispec.SVSpecificationTest;
import client.clientlibrary.rvsi.vc.AbstractVersionConstraintTest;
import client.clientlibrary.rvsi.vc.VCEntryTest;

@RunWith(Suite.class)
@SuiteClasses({
	VCEntryTest.class,
	AbstractRVSISpecificationTest.class,
	BVSpecificationTest.class,
	SVSpecificationTest.class,
	AbstractVersionConstraintTest.class
})
public class RVSITestSuite
{

}
