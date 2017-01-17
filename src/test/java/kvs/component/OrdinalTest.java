package kvs.component;

import static org.junit.Assert.*;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

/**
 * @author hengxin
 * @date Created on Dec 11, 2015
 */
public class OrdinalTest
{
	@NotNull
    private Ordinal original_ord0 = new Ordinal(0);
	@NotNull
    private Ordinal original_ord1 = new Ordinal(0);
	@NotNull
    private Ordinal original_ord2 = original_ord1;

	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void testIncrementAndGet()
	{
		Ordinal ord1_inc = original_ord1.incrementAndGet();
		assertEquals("The original ord0 does not change.", 0, original_ord0.getOrd());
		assertEquals("The original ord1 also increments to 1.", 1, original_ord1.getOrd());
		assertEquals("The original ord2 also increments to 1", 1, original_ord2.getOrd());
		assertEquals("The increased ord is 1.", 1, ord1_inc.getOrd());
		
		Ordinal ord2_inc = original_ord2.incrementAndGet();
		assertEquals("The original ord0 does not change.", 0, original_ord0.getOrd());
		assertEquals("The original ord1 also increments to 2.", 2, original_ord1.getOrd());
		assertEquals("The original ord2 also increments to 2", 2, original_ord2.getOrd());
		assertEquals("The increased ord is 2.", 2, ord2_inc.getOrd());	
	}
	
	@Test
	public void testEquals()
	{
		assertEquals("original_ord0 and original_ord1 are equal.", original_ord0, original_ord1);

		assertEquals("original_ord0 and original_ord2 are equal.", original_ord0, original_ord2);
	}

}
