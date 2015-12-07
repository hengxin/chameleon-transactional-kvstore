package client.clientlibrary.transaction.txexception;

/**
 * Custom exception for transaction execution.
 * 
 * @author hengxin
 * @date Created on 12-07-2015
 */
public class TransactionException extends Exception
{
	private static final long serialVersionUID = 9065339122312266555L;

	public TransactionException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
