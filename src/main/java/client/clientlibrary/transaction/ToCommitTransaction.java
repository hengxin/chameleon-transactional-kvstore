package client.clientlibrary.transaction;

import com.google.common.base.MoreObjects;

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
	private final BufferedUpdates bufferedUpdates;
	
	public ToCommitTransaction(Timestamp sts, BufferedUpdates updates) {
		this.sts = sts;
		this.bufferedUpdates = updates;
	}

	public Timestamp getSts() {
		return sts;
	}

	public BufferedUpdates getBufferedUpdates() {
		return bufferedUpdates;
	}
	
//	@Override
//	public int hashCode() {
//		return Objects.hash(this.sts, this.bufferedUpdates);
//	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(this.sts)
				.addValue(this.bufferedUpdates)
				.toString();
	}
}
