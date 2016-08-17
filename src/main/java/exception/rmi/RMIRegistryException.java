package exception.rmi;

import membership.site.Member;

/**
 * A {@link RMIRegistryException} is a {@link RuntimeException} 
 * which indicates an error in locating, via RMI registry, 
 * the remote stub of an {@link ISite}.
 * @author hengxin
 * @date Created on Jan 1, 2016
 */
public class RMIRegistryException extends RuntimeException {

	private static final long serialVersionUID = 8066493032842438399L;
	
	private Member member;

	public RMIRegistryException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	public RMIRegistryException(Member member, Throwable cause) {
		this(String.format("Failed to locate remote stub for [%s].", member), cause);
		this.member = member;
	}

	public Member getMember() {
		return member;
	}
}
