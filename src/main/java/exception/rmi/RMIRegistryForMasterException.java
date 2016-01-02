package exception.rmi;

/**
 * A {@link RMIRegistryForMasterException} indicates an error in locating,
 * via RMI registry, a remote stub of a master site.
 * @author hengxin
 * @date Created on Jan 2, 2016
 */
public final class RMIRegistryForMasterException extends RMIRegistryException {

	private static final long serialVersionUID = 2970569487451736353L;

	public RMIRegistryForMasterException(RMIRegistryException rre) {
		super(String.format("Failed to locate remote stub for master [%s]", rre.getMember()), rre.getCause());
	}

}
