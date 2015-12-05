/**
 * 
 */
package client.clientlibrary.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import client.communication.ClientContext;
import client.communication.ClientContextSingleMaster;
import kvs.component.Timestamp;
import master.communication.MasterLauncher;

/**
 * @author hengxin
 * @date 10-29-2015
 * 
 * Test cases for {@link RVSITransaction}
 */
public class RVSITransactionTest
{
	private MasterLauncher master_launcher;
	private ClientContext context;
	private ITransaction tx;
	
	@Before
	public void setUp()
	{
		this.master_launcher = new MasterLauncher();

		this.context = new ClientContextSingleMaster();
		this.tx = new RVSITransaction(context);
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
	
	@After
	public void tearDown()
	{
		this.master_launcher.reclaim();
	}
}
