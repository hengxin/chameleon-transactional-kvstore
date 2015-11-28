/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import client.clientlibrary.rvsi.versionconstraints.AbstractVersionConstraint;
import client.clientlibrary.rvsi.versionconstraints.BVVersionConstraint;
import kvs.component.Timestamp;

/**
 * k1-BV (Backward View) specification.
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 */
public class BVSpecification extends AbstractRVSISpecification
{

	@Override
	public AbstractVersionConstraint generateVersionConstraint(Timestamp ts)
	{
		return new BVVersionConstraint(AbstractRVSISpecification.transform(super.vce_info_list, ts));
	}

}
