package site;

import java.rmi.Remote;
import java.rmi.RemoteException;

import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;

/**
 * Interface {@link ISite} exposes basic (i.e., non-transactional)
 * data access operations like {@link #get(Row, Column)} and 
 * {@link #put(Row, Column, ITimestampedCell)}.
 * These operations are available remotely, by letting this interface 
 * {@link ISite} extend {@link Remote}.
 * @author hengxin
 * @date Created on 12-09-2015
 */
public interface ISite extends Remote {

	/**
	 * Reads from this data provider.
	 * @param r		{@link Row} key
	 * @param c		{@link Column} key.
	 * @return		an {@link ITimestampedCell} corresponding to @param r and @param c
	 * @throws RemoteException	if an error occurs during the RMI call
	 */
    ITimestampedCell get(Row r, Column c) throws RemoteException;
	
	/**
	 * Writes into this data provider.
	 * @param r		{@link Row} key
	 * @param c		{@link Column} key
	 * @param ts_cell	{@link ITimestampedCell} to write, corresponding to @param r and @param c
	 * @return		{@code true} if write successfully; {@code false}, otherwise.
	 * @throws RemoteException	if an error occurs during the RMI call
	 */
    boolean put(Row r, Column c, ITimestampedCell ts_cell) throws RemoteException;

}
