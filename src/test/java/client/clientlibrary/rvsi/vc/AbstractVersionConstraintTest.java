package client.clientlibrary.rvsi.vc;

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

	private AbstractRVSISpecification rvsi_spec = new BVSpecification();
	private QueryResults query_results = new QueryResults();

	private CompoundKey ck_r1_c1 = new CompoundKey("R1", "C1");
	private CompoundKey ck_r1_c2 = new CompoundKey("R1", "C2");
	private CompoundKey ck_r2_c1 = new CompoundKey("R2", "C1");
	private CompoundKey ck_r2_c2 = new CompoundKey("R2", "C2");
	
	private TimestampedCell tc1 = new TimestampedCell(Timestamp.TIMESTAMP_INIT, Ordinal.ORDINAL_INIT, new Cell("R1C1"));
	private TimestampedCell tc2 = new TimestampedCell(Timestamp.TIMESTAMP_INIT, Ordinal.ORDINAL_INIT, new Cell("R2C2"));
	
	@Before
	public void setUp() throws Exception {
		// set the {@value #rvsi_spec}
		HashSet<CompoundKey> ckey_set_r1 = Stream.of(ck_r1_c1, ck_r1_c2)
				.collect(Collectors.toCollection(HashSet::new));
		
		HashSet<CompoundKey> ckey_set_r2 = Stream.of(ck_r2_c1, ck_r2_c2)
				.collect(Collectors.toCollection(HashSet::new));
		
		this.rvsi_spec.addSpec(ckey_set_r1, 1L);
		this.rvsi_spec.addSpec(ckey_set_r2, 2L);
		
		// set the the {@link #query_results}
		this.query_results.put(ck_r1_c1, tc1);
		this.query_results.put(ck_r2_c2, tc2);
	}
	
	@Test
	public void testCheck() {
	}


	@Test
	public void testEquals() {
		Timestamp ts = new Timestamp(1L);
		
		VCEntry vc_entry_r1_c1 = new VCEntry(this.ck_r1_c1, this.tc1.getOrdinal(), ts, 2L);
		VCEntry vc_entry_r2_c2 = new VCEntry(this.ck_r2_c2, this.tc2.getOrdinal(), ts, 4L);
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
