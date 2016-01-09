package rmi;

import exception.SiteException;

/**
 * Interface {@link IRemoteSite} exposes {@link #export()} and {@link #reclaim()}
 * operations for remote RMI accesses. 
 * @author hengxin
 * @date Created on 12-03-2015
 */
public interface IRemoteSite {

	/** 
	 * A remote object (is able to and) exports itself for remote accesses.
	 * @throws SiteException if an error occurs during export.
	 */
	public void export() throws SiteException;
	
	/**
	 * A remote object reclaims itself from remote accesses.
	 * @throws SiteException if an error occurs during reclaim.
	 */
	public void reclaim() throws SiteException;

}
