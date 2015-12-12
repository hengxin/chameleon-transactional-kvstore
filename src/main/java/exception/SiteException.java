package exception;

/**
 * Site-related exception class, for instance,
 * thrown if a site fails to export itself for remote accesses
 * or to reclaim itself from remote accesses.
 * 
 * @author hengxin
 * @date Created on 12-09-2015
 */
public class SiteException extends Exception
{
	private static final long serialVersionUID = 5971234679471968716L;

	public SiteException(String msg, Throwable cause)
	{
		super(msg, cause);
	}
}
