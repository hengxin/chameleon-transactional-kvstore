package exception.rmi;

/**
 * A {@link RMIRegistryForSlaveException} indicates an error in locating,
 * via RMI registry, a remote stub of a slave site.
 * @author hengxin
 * @date Created on Jan 2, 2016
 */
public class RMIRegistryForSlaveException extends RMIRegistryException {

	private static final long serialVersionUID = -4085168808642118822L;

	public RMIRegistryForSlaveException(RMIRegistryException rre) {
		super(String.format("Failed to locate remote stub for slave [%s]", rre.getMember()), rre.getCause());
	}

}
