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
import kvs.component.Timestamp;

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
		AbstractClientContext context = null;
		try
		{
			context = new ClientContextSingleMaster();
		} catch (ContextException | MemberParseException ce)
		{
			LOGGER.error(ce.getMessage());
			System.exit(1);
		}

		ITransaction tx = new RVSITransaction(context);
		assertTrue("Transaction does not begin successfully.", tx.begin());
		assertEquals("Start-timestamp has not been assigned correctly.", new Timestamp(1L), ((RVSITransaction) tx).getSts());
	}

}
