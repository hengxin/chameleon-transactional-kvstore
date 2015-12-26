package exception.transaction;

/**
 * Exception indicating failures of a transactional read operation.
 * @author hengxin
 * @date Created on Dec 12, 2015
 */
public class TransactionReadException extends TransactionExecutionException
{
	private static final long serialVersionUID = 7167807521238568552L;

	public TransactionReadException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
