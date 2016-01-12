package exception.transaction;

/**
 * {@link TransactionCommunicationException} is a {@link RuntimeException} 
 * indicates errors which occurred during communications, including 
 * both the communication between clients and the master via RMI and that between
 * the master and its slaves via JMS.
 * @author hengxin
 * @date Created on Dec 11, 2015
 * @see	{@link TransactionExecutionException}
 */
public class TransactionCommunicationException extends RuntimeException {

	private static final long serialVersionUID = 6045821857590451132L;

	public TransactionCommunicationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
