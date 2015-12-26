package exception.transaction;

/**
 * Exception indicating failure of beginning a transaction.
 * @author hengxin
 * @date Created on Dec 12, 2015
 */
public class TransactionBeginException extends TransactionExecutionException
{
	private static final long serialVersionUID = -4904706412975758161L;

	public TransactionBeginException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
