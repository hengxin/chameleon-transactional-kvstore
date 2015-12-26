package exception.transaction;

/**
 * Exception indicating errors occurred during communications, including both 
 * the communication between clients and the master via RMI and that between
 * the master and its slaves via JMS.
 * 
 * @author hengxin
 * @date Created on Dec 11, 2015
 * 
 * @see	{@link TransactionExecutionException}
 */
public class TransactionCommunicationException extends Exception
{
	private static final long serialVersionUID = 6045821857590451132L;

	public TransactionCommunicationException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
