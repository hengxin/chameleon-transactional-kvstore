/**
 * 
 */
package client.clientlibrary.transaction;

import kvs.table.Cell;
import kvs.table.Column;
import kvs.table.Row;

/**
 * @author hengxin
 * @date 10-27-2015
 */
public interface ITransaction
{
	public boolean begin();
	public boolean read(Row r, Column c);
	public boolean write(Row r, Column c, Cell data);
	public boolean end();
}
