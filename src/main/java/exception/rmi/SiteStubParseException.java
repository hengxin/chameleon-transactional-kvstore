package exception.rmi;

/**
 * Unchecked exception indicating an error in parsing remote site stubs. 
 * @author hengxin
 * @date Created on Jan 1, 2016
 */
public class SiteStubParseException extends RuntimeException {

	private static final long serialVersionUID = 8066493032842438399L;

	public SiteStubParseException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
