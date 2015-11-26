package slave;

import client.clientlibrary.transaction.ToCommitTransaction;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;

/**
 * Interface for the slave sites.
 * 
 * @author hengxin
 * @date Created 10-28-2015
 */
public interface ISlave
{
	public abstract ITimestampedCell read(Row row, Column col);
	public abstract void apply(ToCommitTransaction tx);
}
