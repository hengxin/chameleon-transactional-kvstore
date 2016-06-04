package twopc.coordinator;

import java.util.Map;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import client.context.AbstractClientContext;
import site.ISite;

/**
 * {@link RVSI2PCSequentialLockingCoordinator} is a 2PC coordinator for committing
 * RVSI distributed transactions. This coordinator employs sequential locking strategy
 * in its first phase to avoid potential global deadlocks when multiple coordinators
 * are executing 2PC.
 * @author hengxin
 * @date Created on Jan 13, 2016
 */
public final class RVSI2PCSequentialLockingCoordinator extends Abstract2PCCoordinator {
	
	private final VersionConstraintManager vcm;

	public RVSI2PCSequentialLockingCoordinator(AbstractClientContext ctx, VersionConstraintManager vcm) {
		super(ctx);
		this.vcm = vcm;
	}
	
	@Override
	public boolean execute2PC(ToCommitTransaction tx) {
		final Map<ISite, ToCommitTransaction> site_tx_map = super.ctx.partition(tx);
		// TODO split the vcm
		
		
		return false;
	}

    @Override
    public boolean onPreparePhaseFinished() {
        return false;
    }

    @Override
    public boolean onCommitPhaseFinished() {
        return false;
    }

}
