package rmi;

/**
 * Using Java-RMI as the mechanism of synchronous communication
 * between clients and the remote sites.
 * 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public interface IRMI
{
	/** 
	 * A remote object should export itself for remote accesses.
	 * @return {@code true} if no errors occur in the export; {@code false}, otherwise.
	 */
	public boolean export();
}
