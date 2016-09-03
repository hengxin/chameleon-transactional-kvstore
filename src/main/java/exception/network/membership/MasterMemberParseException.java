package exception.network.membership;

/**
 * {@link MasterMemberParseException} indicates an error occurring
 * during parsing a master {@link Member}.
 * @author hengxin
 * @date Created on Jan 1, 2016
 * @see {@link SlaveMemberParseException}
 * @deprecated not used any more
 */
@Deprecated
public class MasterMemberParseException extends MemberParseException {

	private static final long serialVersionUID = 4267885981567197079L;

	public MasterMemberParseException(Throwable cause) {
		super("Fails to parseReplGrps master!", cause);
	}

}
