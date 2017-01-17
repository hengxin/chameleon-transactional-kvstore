/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import org.jetbrains.annotations.NotNull;

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
	@NotNull
    @Override
	public AbstractVersionConstraint generateVersionConstraint(Timestamp ts) {
		return new FVVersionConstraint(AbstractRVSISpecification.transform(super.vceInfos, ts));
	}

}
