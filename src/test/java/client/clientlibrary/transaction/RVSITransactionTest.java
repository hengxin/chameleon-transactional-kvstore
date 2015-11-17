/**
 * 
 */
package client.clientlibrary.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

import client.clientlibrary.transaction.ITransaction;
import client.clientlibrary.transaction.RVSITransaction;
import kvs.component.Timestamp;

/**
 * @author hengxin
 * @date 10-29-2015
 * 
 * Test cases for {@link RVSITransaction}
 */
public class RVSITransactionTest
{

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
	}

	/**
	 * Test method for {@link client.clientlibrary.transaction.RVSITransaction#begin()}.
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testBegin() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
	{
		ITransaction tx = new RVSITransaction();
		assertTrue("Transaction does not begin successfully.", tx.begin());
		
		assertEquals("Start-timestamp has not been assigned correctly.", new Timestamp(1L), ((RVSITransaction) tx).getSts());
	}

	/**
	 * Test method for {@link client.clientlibrary.transaction.RVSITransaction#read(kvs.table.Row, kvs.table.Column)}.
	 */
	@Test
	public void testRead()
	{
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link client.clientlibrary.transaction.RVSITransaction#write(kvs.table.Row, kvs.table.Column, kvs.table.Cell)}.
	 */
	@Test
	public void testWrite()
	{
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link client.clientlibrary.transaction.RVSITransaction#end()}.
	 */
	@Test
	public void testEnd()
	{
		fail("Not yet implemented"); // TODO
	}
}
