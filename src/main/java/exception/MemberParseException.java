package exception;

/**
 * Exception indicates errors in parsing {@link Member}.
 * 
 * @author hengxin
 * @date Created on 12-10-2015
 */
public class MemberParseException extends Exception
{
	private static final long serialVersionUID = 1002517245882512187L;
	
	public MemberParseException(String msg)
	{
		super(msg);
	}
	
	public MemberParseException(String msg, Throwable cause)
	{
		super(msg, cause);
	}

}
