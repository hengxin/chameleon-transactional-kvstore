package client.clientlibrary.rvsi.vc;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Test {@link VCEntryTest#equals(Object)}.
 * @author hengxin
 * @date Created on Dec 30, 2015
 */
public class VCEntryTest {
	private final CompoundKey ck = new CompoundKey("R1", "C1");
	private final Ordinal ord = new Ordinal(1);
	private final Timestamp ts = new Timestamp(1);
	private final int bound = 1;
	@NotNull
    private VCEntry vc_entry = new VCEntry(ck, ord, ts, bound);
	
	private final CompoundKey ck1 = new CompoundKey("R1", "C1");
	private final Ordinal ord1 = new Ordinal(1);
	private final Timestamp ts1 = new Timestamp(1);
	private final int bound1 = 1;
	@NotNull
    private VCEntry vc_entry1 = new VCEntry(ck1, ord1, ts1, bound1);

	private final CompoundKey ck2 = new CompoundKey("R2", "C2");
	private final Ordinal ord2 = new Ordinal(2);
	private final Timestamp ts2 = new Timestamp(2);
	private final int bound2 = 2;
	@NotNull
    private VCEntry vc_entry2 = new VCEntry(ck2, ord2, ts2, bound2);

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testEqualsObject() {
		assertNotSame("vc_entry is not the same as vc_entry1.", vc_entry, vc_entry1);
		assertEquals("vc_entry and vc_entry1 are equal.", vc_entry1, vc_entry);
		
		assertNotEquals("vc_entry and vc_entry2 are not equal.", vc_entry2, vc_entry);
		
		assertNotEquals("vc_entry and null are not equal.", null, vc_entry);
	}

}
