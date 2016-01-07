package client.clientlibrary.transaction;

import com.google.common.base.MoreObjects;

import static org.junit.Assert.assertEquals;

import java.util.Objects;

import kvs.component.Timestamp;
import messages.AbstractMessage;

/**
 * @author hengxin
 * @date Created on 11-23-2015
 * 
 * <p> To represent the transaction which is about to commit;
 * It consists of a transaction's start-timestamp and buffered updates.
 */
public class ToCommitTransaction extends AbstractMessage {

	private static final long serialVersionUID = -137070517043340731L;

	private final Timestamp sts;
	private final BufferedUpdates updates;
	
	public ToCommitTransaction(Timestamp sts, BufferedUpdates updates) {
		this.sts = sts;
		this.updates = updates;
	}

	public Timestamp getSts() {
		return sts;
	}

	public BufferedUpdates getBufferedUpdates() {
		return updates;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.sts, this.updates);
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this)
			return true;
		if(o == null)
			return false;
		if(o.getClass() != this.getClass())
			return false;
		
		ToCommitTransaction that = (ToCommitTransaction) o;
		
		return Objects.equals(this.sts, that.sts)
				&& Objects.equals(this.updates, that.updates);
	}
	
	/**
	 * Merges two {@link ToCommitTransaction}s and returns a new one. The original {@link ToCommitTransaction}s
	 * are not modified.
	 * @param first_tx	{@link ToCommitTransaction} to be merged
	 * @param second_tx {@link ToCommitTransaction} to be merged
	 * @return	a new {@link ToCommitTransaction}
	 * 
	 * @throws AssertionError if the two {@link ToCommitTransaction}s to be merged have different (start-)timestamps.
	 */
	public static ToCommitTransaction merge(ToCommitTransaction first_tx, ToCommitTransaction second_tx) {
		Timestamp sts = first_tx.sts;
		assertEquals("Two ToCommitTransactions be to merged should have the same timestamp.", sts, second_tx.sts);
		return new ToCommitTransaction(sts, BufferedUpdates.merge(first_tx.updates, second_tx.updates));
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(this.sts)
				.addValue(this.updates)
				.toString();
	}
}
