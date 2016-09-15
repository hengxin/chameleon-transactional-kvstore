/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import org.jetbrains.annotations.NotNull;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.rvsi.vc.BVVersionConstraint;
import kvs.component.Timestamp;

/**
 * k1-BV (Backward View) specification.
 * 
 * @author hengxin
 * @date Created on 10-27-2015
 */
public class BVSpecification extends AbstractRVSISpecification {
	@NotNull
    @Override
	public AbstractVersionConstraint generateVersionConstraint(Timestamp ts) {
		return new BVVersionConstraint(AbstractRVSISpecification.transform(super.vceInfos, ts));
	}

}
