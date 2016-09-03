package exception;

/**
 * A {@link SiteException} is a {@link RuntimeException} that TODO
 * @author hengxin
 * @date Created on Jan 11, 2016
 */
public class SiteException extends RuntimeException {

	private static final long serialVersionUID = 1392038805996776185L;
	
	public SiteException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
