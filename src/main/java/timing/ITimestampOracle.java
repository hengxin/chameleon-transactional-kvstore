package timing;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The timestamp oracle is a server that hands out 
 * timestamps in strictly increasing order.
 * @author hengxin
 * @date Created on Dec 27, 2015
 */
public interface ITimestampOracle extends Remote {
	int get() throws RemoteException;
}
