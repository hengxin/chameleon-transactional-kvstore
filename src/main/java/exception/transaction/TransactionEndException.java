package exception.transaction;

/**
 * Exception indicating failures arising from trying to commit a transaction.
 * @author hengxin
 * @date Created on Dec 12, 2015
 */
public class TransactionEndException extends TransactionExecutionException
{
	private static final long serialVersionUID = -6054468801940031450L;

	public TransactionEndException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

}
