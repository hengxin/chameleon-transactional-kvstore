/**
 * 
 */
package client.clientlibrary.transaction;

import exception.transaction.TransactionEndException;
import exception.transaction.TransactionReadException;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;

/**
 * Transactional operations issued at the client side, 
 * including {@link #begin()}, {@link #read(Row, Column)},
 * {@link #write(Row, Column, Cell)}, and {@link #end()}.
 * @author hengxin
 * @date 10-27-2015
 */
public interface ITransaction {
	boolean begin();
	ITimestampedCell read(Row r, Column c) throws TransactionReadException;
	boolean write(Row r, Column c, Cell data);
	boolean end() throws TransactionEndException;
}
