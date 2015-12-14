package rmi;

import exception.SiteException;

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
	 * A remote object (is able to and) exports itself for remote accesses.
	 * 
	 * @throws SiteException if an error occurs during export.
	 */
	public void export() throws SiteException;
	
	/**
	 * A remote object reclaims itself from remote accesses.
	 * 
	 * @throws SiteException if an error occurs during reclaim.
	 */
	public void reclaim() throws SiteException;
}
