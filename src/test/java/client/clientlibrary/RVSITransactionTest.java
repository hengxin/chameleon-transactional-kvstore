/**
 * 
 */
package client.clientlibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.BeforeClass;
import org.junit.Test;

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
	 * Test method for {@link client.clientlibrary.RVSITransaction#begin()}.
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
		
		assertEquals("Start-timestamp has not been assigned correctly.", 1, ((RVSITransaction) tx).getSts());
	}

	/**
	 * Test method for {@link client.clientlibrary.RVSITransaction#read(kvs.table.Row, kvs.table.Column)}.
	 */
	@Test
	public void testRead()
	{
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link client.clientlibrary.RVSITransaction#write(kvs.table.Row, kvs.table.Column, kvs.table.Cell)}.
	 */
	@Test
	public void testWrite()
	{
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link client.clientlibrary.RVSITransaction#end()}.
	 */
	@Test
	public void testEnd()
	{
		fail("Not yet implemented"); // TODO
	}

}
