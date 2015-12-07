package exception;

/**
 * Context ({@link MasterContext}, {@link SlaveContext}, {@link ClientContext}) related exceptions.
 * 
 * @author hengxin
 * @date Created on 12-07-2015
 */
public class ContextException extends Exception
{
	private static final long serialVersionUID = 945715598605270587L;

	public ContextException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
