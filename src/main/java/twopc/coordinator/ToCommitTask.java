package twopc.coordinator;

import java.util.concurrent.Phaser;

import client.clientlibrary.rvsi.rvsimanager.VersionConstraintManager;
import client.clientlibrary.transaction.ToCommitTransaction;
import site.ISite;

/**
 * Two-phase commit protocol consists of "prepare" and "commit".
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public class ToCommitTask implements Runnable
{
	private final Phaser phaser;
	private final ISite site;
	private final ToCommitTransaction tx;
	private final VersionConstraintManager vcm;

	public ToCommitTask(final Phaser phaser, final ISite site, final ToCommitTransaction tx, final VersionConstraintManager vcm)
	{
		this.phaser = phaser;
		this.site = site;
		this.tx = tx;
		this.vcm = vcm;
	}

	@Override
	public void run()
	{
		
	}
}