package client.clientlibrary.transaction;

import com.google.common.base.MoreObjects;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

import client.clientlibrary.partitioning.IPartitioner;
import kvs.component.Timestamp;
import messaging.AbstractMessage;
import site.ISite;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

/**
 * @author hengxin
 * @date Created on 11-23-2015
 * 
 * <p> To represent the transaction which is about to commit;
 * It consists of a transaction's start-timestamp and buffered updates.
 */
public class ToCommitTransaction extends AbstractMessage {
	private static final long serialVersionUID = -137070517043340731L;

	@NotNull private final Timestamp sts;
    private Timestamp cts;
	@NotNull private final BufferedUpdates updates;
	
	public ToCommitTransaction(@NotNull Timestamp sts, @NotNull BufferedUpdates updates) {
		this.sts = sts;
		this.updates = updates;
	}

	@NotNull
    public Timestamp getSts() { return sts; }
    public Timestamp getCts() { return cts; }
    public void setCts(@NotNull final Timestamp cts) { this.cts = cts; }

    @NotNull
    public BufferedUpdates getBufferedUpdates() { return updates; }
	
	@Override
	public int hashCode() {
		return Objects.hash(sts, updates);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null || o.getClass() != this.getClass())
			return false;

		ToCommitTransaction that = (ToCommitTransaction) o;
		
		return Objects.equals(this.sts, that.sts)
				&& Objects.equals(this.updates, that.updates);
	}

    /**
     * Partition a {@link ToCommitTransaction} into multiple ones, according to
     * {@link IPartitioner}.
     *
     * @param partitioner instance of {@link IPartitioner}
     * @param buckets	number of buckets (i.e., {@link ISite})
     * @return a map from the index of an {@link ISite} to the sub-{@link ToCommitTransaction} it is responsible for
     *
     * @see <a href="http://stackoverflow.com/q/34648849/1833118">groupingBy and collectingAndThen@stackoverflow</a>
     */
    public Map<Integer, ToCommitTransaction> partition(@NotNull IPartitioner partitioner, int buckets) {
        return getBufferedUpdates().stream()
                .collect(groupingBy(item -> partitioner.locateSiteIndexFor(item.getCK(), buckets),
                        collectingAndThen(toList(), items -> new ToCommitTransaction(sts, new BufferedUpdates(items)))));
    }

	/**
	 * Merges two {@link ToCommitTransaction}s and returns a new one. The original {@link ToCommitTransaction}s
	 * are not modified.
	 * @param firstTx	{@link ToCommitTransaction} to be merged
	 * @param secondTx {@link ToCommitTransaction} to be merged
	 * @return	a new {@link ToCommitTransaction}
	 * 
	 * @throws AssertionError if the two {@link ToCommitTransaction}s to be merged have different (start-)timestamps.
	 */
	@NotNull
    public static ToCommitTransaction merge(@NotNull ToCommitTransaction firstTx, @NotNull ToCommitTransaction secondTx) {
		Timestamp sts = firstTx.sts;
		assertEquals("Two ToCommitTransactions be to merged should have the same timestamp.", sts, secondTx.sts);
		return new ToCommitTransaction(sts, BufferedUpdates.merge(firstTx.updates, secondTx.updates));
	}

	@NotNull
    @Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(sts)
                .addValue(cts)
				.addValue(updates)
				.toString();
	}

}
