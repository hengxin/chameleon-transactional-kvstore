package client.clientlibrary.rvsi.rvsispec;

import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.rvsi.vc.BVVersionConstraint;
import client.clientlibrary.rvsi.vc.VCEntry;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Cell;
import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.TimestampedCell;

import static org.junit.Assert.assertEquals;

public final class BVSpecificationTest {

	@NotNull
    private AbstractRVSISpecification rvsi_spec = new BVSpecification();
	@NotNull
    private QueryResults query_results = new QueryResults();
	
	@NotNull
    private CompoundKey ck_r1_c1 = new CompoundKey("R1", "C1");
	@NotNull
    private CompoundKey ck_r1_c2 = new CompoundKey("R1", "C2");
	@NotNull
    private CompoundKey ck_r2_c1 = new CompoundKey("R2", "C1");
	@NotNull
    private CompoundKey ck_r2_c2 = new CompoundKey("R2", "C2");

	@NotNull
    private TimestampedCell tc1 = new TimestampedCell(Timestamp.TIMESTAMP_INIT, new Ordinal(1L), new Cell("R1C1"));
	@NotNull
    private TimestampedCell tc2 = new TimestampedCell(Timestamp.TIMESTAMP_INIT, new Ordinal(2L), new Cell("R2C2"));
	
	@NotNull
    private Timestamp sts = new Timestamp(5L);
	
	@Before
	public void setUp() throws Exception {
		HashSet<CompoundKey> ck_grp_r1 = new HashSet<>();
		ck_grp_r1.add(ck_r1_c1);
		ck_grp_r1.add(ck_r1_c2);
		
		HashSet<CompoundKey> ck_grp_r2 = new HashSet<>();
		ck_grp_r2.add(ck_r2_c1);
		ck_grp_r2.add(ck_r2_c2);
		
		this.rvsi_spec.addSpec(ck_grp_r1, 1);
		this.rvsi_spec.addSpec(ck_grp_r2, 2);
		
		this.query_results.put(ck_r1_c1, tc1);
		this.query_results.put(ck_r2_c2, tc2);
	}

	@Test
	public void testGenerateVersionConstraint() {
		AbstractVersionConstraint bv_vc = this.rvsi_spec.generateVersionConstraint(this.query_results, this.sts);
		
		VCEntry vce_r1_c1 = new VCEntry(this.ck_r1_c1, new Ordinal(1), this.sts, 1);
		VCEntry vce_r2_c2 = new VCEntry(this.ck_r2_c2, new Ordinal(2), this.sts, 2);
		
		List<VCEntry> expected_vc_entry_list = new ArrayList<>();
		expected_vc_entry_list.add(vce_r1_c1);
		expected_vc_entry_list.add(vce_r2_c2);

		AbstractVersionConstraint expected_bv_vc = new BVVersionConstraint(expected_vc_entry_list);
		
		assertEquals("These two BV-VC should be equal.", bv_vc, expected_bv_vc);
	}

}
