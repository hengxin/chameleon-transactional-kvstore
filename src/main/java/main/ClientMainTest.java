package main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import client.clientlibrary.transaction.ITransaction;
import client.clientlibrary.transaction.RVSITransaction;
import client.context.ClientContext;
import client.context.ClientContextSingleMaster;
import kvs.component.Timestamp;

/**
 * Main class at the client side. 
 * It mainly tests {@link ClientContext} and {@link RVSITransaction}
 * in the <i>single-master-multiple-slaves</i> setting.
 * 
 * @author hengxin
 * @date Created on 12-05-2015
 */
public class ClientMainTest
{
	public static void main(String[] args)
	{
		ClientContext context = new ClientContextSingleMaster();
		ITransaction tx = new RVSITransaction(context);

		assertTrue("Transaction does not begin successfully.", tx.begin());
		
		assertEquals("Start-timestamp has not been assigned correctly.", new Timestamp(1L), ((RVSITransaction) tx).getSts());
	}

}