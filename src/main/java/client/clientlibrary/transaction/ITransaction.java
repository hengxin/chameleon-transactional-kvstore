/**
 * 
 */
package client.clientlibrary.transaction;

import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;

/**
 * @author hengxin
 * @date 10-27-2015
 */
public interface ITransaction
{
	public boolean begin();
	public ITimestampedCell read(Row r, Column c);
	public boolean write(Row r, Column c, Cell data);
	public boolean end();
}
