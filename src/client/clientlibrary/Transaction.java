/**
 * 
 */
package client.clientlibrary;

import kvs.table.Cell;
import kvs.table.Column;
import kvs.table.Row;

/**
 * @author hengxin
 * @date 10-27-2015
 */
public interface Transaction
{
	public void begin();
	public void read(Row r, Column c);
	public void write(Row r, Column c, Cell data);
	public void end();
}
