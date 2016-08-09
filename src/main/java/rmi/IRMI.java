package rmi;

import exception.rmi.RMIRegistryException;

/**
 * Interface {@link IRMI} exposes {@link #export()} and {@link #reclaim()}
 * operations for remote RMI accesses. 
 * @author hengxin
 * @date Created on 12-03-2015
 * FIXME alternative: replaced by "extends {@link UnicastRemoteObject}"
 */
public interface IRMI {

    int RMI_REGISTRY_PORT = 1099;

	/**
	 * A remote object (is able to and) exports itself for remote accesses.
	 * @throws RMIRegistryException if an error occurs during export.
	 */
    void export();
	
	/**
	 * A remote object reclaims itself from remote accesses.
	 * @throws RMIRegistryException if an error occurs during reclaim.
	 */
    void reclaim();

}
