package master.mvcc;

import java.sql.Timestamp;

import client.clientlibrary.transaction.BufferedUpdates;
import intervaltree.IntervalTree;

/**
 * @author hengxin
 * @date 11-14-2015
 * 
 * <p> Logs of committed transactions, each of which consisting of 
 * its start-timestamp (sts), commit-timestamp (cts), and updates (of {@link BufferedUpdates}).
 * 
 * <p> {@link StartCommitLogs} will be used to check write-conflicts among transactions.
 * 
 * <b>Important note:</b> The {@link IntervalTree} implementation used here is credited to gds12/IntervalTree
 * (see <a href="https://github.com/gds12/IntervalTree">gds12/IntervalTree AT GitHub</a>).
 */
public class StartCommitLogs
{
	private IntervalTree<Timestamp, BufferedUpdates> start_commit_logs = new IntervalTree<>();
	
	/**
	 * adding a new start-commit-log of a transaction
	 * @param sts start-timestamp 
	 * @param cts commit-timestamp
	 * @param updates buffered updates
	 */
	public void addStartCommitLog(Timestamp sts, Timestamp cts, BufferedUpdates updates)
	{
		this.start_commit_logs.put(sts, cts, updates);
	}
}
