/**
 * 
 */
package client.clientlibrary.transaction;

import exception.transaction.TransactionBeginException;
import exception.transaction.TransactionEndException;
import exception.transaction.TransactionReadException;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.compound.ITimestampedCell;
import twopc.TwoPCResult;

/**
 * Transactional operations issued at the client side, 
 * including {@link #begin()}, {@link #read(Row, Column)},
 * {@link #write(Row, Column, Cell)}, and {@link #end()}.
 * @author hengxin
 * @date 10-27-2015
 */
public interface ITransaction {
	boolean begin() throws TransactionBeginException;
	ITimestampedCell read(Row r, Column c) throws TransactionReadException;
	void write(Row r, Column c, Cell data);
	TwoPCResult end() throws TransactionEndException;
}
