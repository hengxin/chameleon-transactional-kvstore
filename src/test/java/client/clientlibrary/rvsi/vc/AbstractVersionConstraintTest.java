package client.clientlibrary.rvsi.vc;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification;
import client.clientlibrary.rvsi.rvsispec.BVSpecification;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Cell;
import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.TimestampedCell;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AbstractVersionConstraintTest {

	@NotNull
    private AbstractRVSISpecification rvsiSpec = new BVSpecification();
	@NotNull
    private QueryResults queryResults = new QueryResults();

	@NotNull
    private CompoundKey ck_r1_c1 = new CompoundKey("R1", "C1");
	@NotNull
    private CompoundKey ck_r1_c2 = new CompoundKey("R1", "C2");
	@NotNull
    private CompoundKey ck_r2_c1 = new CompoundKey("R2", "C1");
	@NotNull
    private CompoundKey ck_r2_c2 = new CompoundKey("R2", "C2");
	
	@NotNull
    private TimestampedCell tc1 = new TimestampedCell(Timestamp.TIMESTAMP_INIT, Ordinal.ORDINAL_INIT(), new Cell("R1C1"));
	@NotNull
    private TimestampedCell tc2 = new TimestampedCell(Timestamp.TIMESTAMP_INIT, Ordinal.ORDINAL_INIT(), new Cell("R2C2"));
	
	@Before
	public void setUp() throws Exception {
		HashSet<CompoundKey> ckey_set_r1 = Stream.of(ck_r1_c1, ck_r1_c2)
				.collect(Collectors.toCollection(HashSet::new));
		
		HashSet<CompoundKey> ckey_set_r2 = Stream.of(ck_r2_c1, ck_r2_c2)
				.collect(Collectors.toCollection(HashSet::new));
		
		rvsiSpec.addSpec(ckey_set_r1, 1);
		rvsiSpec.addSpec(ckey_set_r2, 2);
		
		// set the the {@link #queryResults}
		queryResults.put(ck_r1_c1, tc1);
		queryResults.put(ck_r2_c2, tc2);
	}
	
	@Test
	public void testCheck() {
	}


	@Test
	public void testEquals() {
		Timestamp ts = new Timestamp(1L);
		
		VCEntry vc_entry_r1_c1 = new VCEntry(this.ck_r1_c1, this.tc1.getOrdinal(), ts, 2);
		VCEntry vc_entry_r2_c2 = new VCEntry(this.ck_r2_c2, this.tc2.getOrdinal(), ts, 4);
		List<VCEntry> vc_entry_list = new ArrayList<>();
		vc_entry_list.add(vc_entry_r1_c1);
		vc_entry_list.add(vc_entry_r2_c2);
		AbstractVersionConstraint bv_vc = new BVVersionConstraint(vc_entry_list);
		AbstractVersionConstraint fv_vc = new FVVersionConstraint(vc_entry_list);
		
		List<VCEntry> vc_entry_list_ignore_order = new ArrayList<>();
		vc_entry_list_ignore_order.add(vc_entry_r2_c2);
		vc_entry_list_ignore_order.add(vc_entry_r1_c1);
		AbstractVersionConstraint bv_vc_ignore_order = new BVVersionConstraint(vc_entry_list_ignore_order);
		
		assertNotEquals("Null is not equal to this.", bv_vc, null);
		assertEquals("Element orders should not be important.", bv_vc, bv_vc_ignore_order);
		assertNotEquals("Different VC subclasses are not equal.", bv_vc, fv_vc);
	}
}
