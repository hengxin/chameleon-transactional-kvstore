package exception.network.membership;

/**
 * {@link SlaveMemberParseException} indicates an error occurring
 * during parsing a slave {@link Member}.
 * @author hengxin
 * @date Created on Jan 1, 2016
 * @see {@link MasterMemberParseException}
 * @deprecated not used any more
 */
@Deprecated
public class SlaveMemberParseException extends MemberParseException {

	private static final long serialVersionUID = 5935449312080439973L;

	public SlaveMemberParseException(Throwable cause) {
		super("Failed to parse slave.", cause);
	}

}
