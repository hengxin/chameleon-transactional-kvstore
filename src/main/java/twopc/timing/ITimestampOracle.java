package twopc.timing;

import java.rmi.Remote;
import java.rmi.RemoteException;

import rmi.IRMI;

/**
 * The timestamp oracle is a server that hands out 
 * timestamps in strictly increasing order.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface ITimestampOracle extends IRMI, Remote {
	int get() throws RemoteException;
}
