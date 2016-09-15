package kvs.compound;

import static org.junit.Assert.*;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import kvs.component.Ordinal;

/**
 * @author hengxin
 * @date Created on Dec 11, 2015
 */
public class CKeyToOrdinalTest
{
	@NotNull
    private CKeyToOrdinal index = new CKeyToOrdinal();
	
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testGet()
	{
		CompoundKey ck = new CompoundKey("R", "C");
		Ordinal ord = this.index.lookup(ck);
		assertEquals("Return the ORDINAL_INIT.", 0, ord.getOrd());
		
		Ordinal next_ord = ord.incrementAndGet();
		assertEquals("The next ordinal is 1.", 1, next_ord.getOrd());
		assertEquals("The original ordinal also changes to 1.", 1, ord.getOrd());
		
		Ordinal ord_second_get = this.index.lookup(ck);
		assertSame("Should return the same Ordinal instance.", ord, ord_second_get);
		assertEquals("The second lookup returns ordinal = 1.", 1, ord_second_get.getOrd());
	}
}
