package twopc.coordinator;

/**
 * Interface for 2PC coordinator which coordinates the two-phase 
 * commit process among the masters involved.
 * <p>
 * The client who issues the transaction to commit plays the role of coordinator.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface ICoordinator
{
	/**
	 * The coordinator executes 2pc protocol.
	 * @return {@code true} if 2pc protocol succeeds in committing; {@code false}, otherwise.
	 */
	public abstract boolean execute2PC();
}
