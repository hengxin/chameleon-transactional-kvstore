package twopc.timing;

/**
 * The timestamp oracle is a server that hands out 
 * timestamps in strictly increasing order.
 * @author hengxin
 * @date Created on Dec 27, 2015
 * 
 * TODO extends Remote???
 */
public interface ITimestampOracle
{
	public abstract int get(); 
}
