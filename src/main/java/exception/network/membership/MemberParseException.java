package exception.network.membership;

/**
 * {@link MemberParseException} is an unchecked exception thrown
 * if an errors occurs during parsing {@link Member}.
 * <p> It has two more concrete subclasses: {@link MasterMemberParseException}
 * and {@link SlaveMemberParseException}.
 * @author hengxin
 * @date Created on 12-10-2015
 * @deprecated not used any more
 */
@Deprecated
public class MemberParseException extends RuntimeException {

	private static final long serialVersionUID = 1002517245882512187L;
	
	public MemberParseException(String msg) {
		super(msg);
	}
	
	public MemberParseException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
