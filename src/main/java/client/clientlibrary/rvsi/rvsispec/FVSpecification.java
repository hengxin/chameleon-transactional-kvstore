/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.rvsi.vc.FVVersionConstraint;
import kvs.component.Timestamp;

/**
 * k2-FV (Forward-View) specification.
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 */
public class FVSpecification extends AbstractRVSISpecification {
	@Override
	public AbstractVersionConstraint generateVersionConstraint(Timestamp ts) {
		return new FVVersionConstraint(AbstractRVSISpecification.transform(super.vceInfos, ts));
	}

}
