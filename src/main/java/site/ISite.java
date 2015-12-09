package site;

import java.rmi.Remote;
import java.rmi.RemoteException;

import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;

/**
 * An {@link ISite} is a server which supports at least the read operations,
 * and can be remotely accessed.
 *
 * @author hengxin
 * @date Created on 12-09-2015
 */
public interface ISite extends Remote
{
	public abstract ITimestampedCell read(Row r, Column c) throws RemoteException;
}
