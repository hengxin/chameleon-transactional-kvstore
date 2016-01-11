package rmi;

/**
 * Interface {@link IRMI} exposes {@link #export()} and {@link #reclaim()}
 * operations for remote RMI accesses. 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public interface IRMI {

	/** 
	 * A remote object (is able to and) exports itself for remote accesses.
	 * @throws RMIRegistryException if an error occurs during export.
	 */
	public void export();
	
	/**
	 * A remote object reclaims itself from remote accesses.
	 * @throws RMIRegistryException if an error occurs during reclaim.
	 */
	public void reclaim();

}
