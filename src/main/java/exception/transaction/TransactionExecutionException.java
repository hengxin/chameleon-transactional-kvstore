package exception.transaction;

/**
 * {@link TransactionExecutionException} is an unchecked {@link RuntimeException}
 * indicating failures occurred during normal transaction execution, 
 * including read, start, and commit.
 * 
 * @author hengxin
 * @date Created on 12-07-2015
 * 
 * @see {@link TransactionCommunicationException}
 */
public class TransactionExecutionException extends RuntimeException {
	private static final long serialVersionUID = 9065339122312266555L;

	public TransactionExecutionException(String msg, Throwable cause) { super(msg, cause); }

}
