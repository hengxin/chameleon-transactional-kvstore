package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.clientlibrary.transaction.ITransaction;
import client.clientlibrary.transaction.RVSITransaction;
import client.context.AbstractClientContext;
import client.context.ClientContextSingleMaster;
import exception.ContextException;
import exception.MemberParseException;
import exception.transaction.TransactionReadException;
import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;

/**
 * Main class at the client side. 
 * It mainly tests {@link AbstractClientContext} and {@link RVSITransaction}
 * in the <i>single-master-multiple-slaves</i> setting.
 * 
 * @author hengxin
 * @date Created on 12-05-2015
 */
public class ClientMainTest
{
	private final static Logger LOGGER = LoggerFactory.getLogger(ClientMainTest.class);
	
	public static void main(String[] args)
	{
		// context
		AbstractClientContext context = null;
		try
		{
			context = new ClientContextSingleMaster();
		} catch (ContextException | MemberParseException ce)
		{
			LOGGER.error(ce.getMessage(), ce.getCause());
			System.exit(1);
		}

		// create transaction
		ITransaction tx = new RVSITransaction(context);
		
		// begin
		assertTrue("Transaction does not begin successfully.", tx.begin());
		assertEquals("Start-timestamp has not been assigned correctly.", new Timestamp(1L), ((RVSITransaction) tx).getSts());
		
		// read
		Row r = new Row("R");
		Column c = new Column("C");
		
		try
		{
			ITimestampedCell ts_cell = tx.read(r, c);
			LOGGER.info("Read {} from {} + {}.", ts_cell, r, c);
		} catch (TransactionReadException tre)
		{
			LOGGER.error(tre.getMessage(), tre.getCause());
			System.exit(1);
		}

		// write
		tx.write(r, c, new Cell("RC11"));

		// end
	}

}
