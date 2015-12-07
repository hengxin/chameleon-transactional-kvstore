package client.clientlibrary.rvsi.rvsispec;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import client.clientlibrary.rvsi.vc.AbstractVersionConstraint;
import client.clientlibrary.rvsi.vc.SVVersionConstraint;
import client.clientlibrary.rvsi.vc.VCEntry;
import client.clientlibrary.rvsi.vc.VCEntryRawInfo;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Cell;
import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.ITimestampedCell;
import kvs.compound.KVItem;
import kvs.compound.TimestampedCell;

public class SVSpecificationTest
{
	private final SVSpecification sv_rvsi_spec = new SVSpecification();
	
	private final CompoundKey ck_rt_ct = new CompoundKey("Rt", "Ct");
	private final CompoundKey ck_rs_cs = new CompoundKey("Rs", "Cs");
	private final HashSet<CompoundKey> ck_set_ts = new HashSet<>();
	private long bound_ts = 1;

	private final CompoundKey ck_rw_cw = new CompoundKey("Rw", "Cw");
	private final CompoundKey ck_rx_cx = new CompoundKey("Rx", "Cx");
	private final CompoundKey ck_ry_cy = new CompoundKey("Ry", "Cy");
	private final CompoundKey ck_rz_cz = new CompoundKey("Rz", "Cz");
	private final HashSet<CompoundKey> ck_set_wxyz = new HashSet<>();
	private long bound_wxyz = 2;

	private final CompoundKey ck_ru_cu = new CompoundKey("Ru", "Cu");
	private final CompoundKey ck_rv_cv = new CompoundKey("Rv", "Cv");
	private final HashSet<CompoundKey> ck_set_uv = new HashSet<>(); 
	private long bound_uv = 3;
	
	private ITimestampedCell ts_cell_t = new TimestampedCell(new Timestamp(1), new Ordinal(1), new Cell("Cell_t"));

	private ITimestampedCell ts_cell_x = new TimestampedCell(new Timestamp(2), new Ordinal(2), new Cell("Cell_x"));
	private ITimestampedCell ts_cell_y = new TimestampedCell(new Timestamp(3), new Ordinal(3), new Cell("Cell_y"));
	private ITimestampedCell ts_cell_z = new TimestampedCell(new Timestamp(4), new Ordinal(4), new Cell("Cell_z"));

	private ITimestampedCell ts_cell_u = new TimestampedCell(new Timestamp(5), new Ordinal(5), new Cell("Cell_u"));
	private ITimestampedCell ts_cell_v = new TimestampedCell(new Timestamp(6), new Ordinal(6), new Cell("Cell_v"));

	private final QueryResults query_results = new QueryResults();
	private final SortedSet<KVItem> kv_set = new TreeSet<>();
	private final List<VCEntryRawInfo> vce_info_xyz_list = new ArrayList<>();
	private final List<VCEntryRawInfo> vce_info_xyzuv_list = new ArrayList<>();
	private final List<VCEntry> vce_xyzuv_list = new ArrayList<>();
	private AbstractVersionConstraint sv_vc = null;
	
	@Before
	public void setUp() throws Exception
	{
		this.ck_set_ts.add(ck_rt_ct);
		this.ck_set_ts.add(ck_rs_cs);
		
		this.ck_set_wxyz.add(ck_rw_cw);
		this.ck_set_wxyz.add(ck_rx_cx);
		this.ck_set_wxyz.add(ck_ry_cy);
		this.ck_set_wxyz.add(ck_rz_cz);
		
		this.ck_set_uv.add(ck_ru_cu);
		this.ck_set_uv.add(ck_rv_cv);
		
	    /**
	     * The {@link QueryResults} is:
	     * { t -> Cell_t, x -> Cell_x, y -> Cell_y, z -> Cell_z, u -> Cell_u, v -> Cell_v },
	     */
		this.query_results.put(ck_rt_ct, ts_cell_t);
		
		this.query_results.put(ck_rx_cx, ts_cell_x);
		this.query_results.put(ck_ry_cy, ts_cell_y);
		this.query_results.put(ck_rz_cz, ts_cell_z);
		
		this.query_results.put(ck_ru_cu, ts_cell_u);
		this.query_results.put(ck_rv_cv, ts_cell_v);
		
		this.kv_set.add(new KVItem(ck_rx_cx, ts_cell_x));
		this.kv_set.add(new KVItem(ck_ry_cy, ts_cell_y));
		this.kv_set.add(new KVItem(ck_rz_cz, ts_cell_z));
		
		VCEntryRawInfo vce_info_x_z_2 = new VCEntryRawInfo(new KVItem(ck_rx_cx, ts_cell_x), new KVItem(ck_rz_cz, ts_cell_z), 2);
		VCEntryRawInfo vce_info_y_z_2 = new VCEntryRawInfo(new KVItem(ck_ry_cy, ts_cell_y), new KVItem(ck_rz_cz, ts_cell_z), 2);
		this.vce_info_xyz_list.add(vce_info_x_z_2);
		this.vce_info_xyz_list.add(vce_info_y_z_2);
		
		VCEntryRawInfo vce_info_u_v_3 = new VCEntryRawInfo(new KVItem(ck_ru_cu, ts_cell_u), new KVItem(ck_rv_cv, ts_cell_v), 3);

		/**
		 *<ol>
		 *<li>[ vce_info_kv = {x, Cell_x}, vce_info_kv_optional = {z, Cell_z}, vce_info_bound = 2]
		 *<li>[ vce_info_kv = {y, Cell_y}, vce_info_kv_optional = {z, Cell_z}, vce_info_bound = 2] 
		 *<li>[ vce_info_kv = {u, Cell_u}, vce_info_kv_optional = {v, Cell_v}, vce_info_bound = 3]
		 *</ol>
		 */
		this.vce_info_xyzuv_list.add(vce_info_x_z_2);
		this.vce_info_xyzuv_list.add(vce_info_y_z_2);
		this.vce_info_xyzuv_list.add(vce_info_u_v_3);
		
		/**
		 * <ol>
		 * <li> [ x, ord(x), ts(z), bound_xyz]
		 * <li> [ y, ord(y), ts(z), bound_xyz ]
		 * <li> [ u, ord(u), ts(v), bound_uv ]
		 * </ol>
		 */
		VCEntry vce_xz = new VCEntry(ck_rx_cx, ts_cell_x.getOrdinal(), ts_cell_z.getTS(), this.bound_wxyz);
		VCEntry vce_yz = new VCEntry(ck_ry_cy, ts_cell_y.getOrdinal(), ts_cell_z.getTS(), this.bound_wxyz);
		VCEntry vce_uv = new VCEntry(ck_ru_cu, ts_cell_u.getOrdinal(), ts_cell_v.getTS(), this.bound_uv);
		
		this.vce_xyzuv_list.add(vce_xz);
		this.vce_xyzuv_list.add(vce_yz);
		this.vce_xyzuv_list.add(vce_uv);
		
		this.sv_vc = new SVVersionConstraint(this.vce_xyzuv_list);
		
		/**
		 * Initialize #sv_rvsi_spec as:
	     * { {t, s} -> 1, {w, x, y, z} -> 2, {u, v} -> 3 }
		 */
		this.sv_rvsi_spec.addSpec(this.ck_set_ts, this.bound_ts);
		this.sv_rvsi_spec.addSpec(this.ck_set_wxyz, this.bound_wxyz);
		this.sv_rvsi_spec.addSpec(this.ck_set_uv, this.bound_uv);
	}

	@Test
	public void testExtractVCEntryRawInfo()
	{
		List<VCEntryRawInfo> expected_vce_info_list = this.vce_info_xyzuv_list;
		List<VCEntryRawInfo> actual_vce_info_list = this.sv_rvsi_spec.extractVCEntryRawInfo(query_results);
		
		assertTrue("Fails to extract three VCEntryRawInfo.", CollectionUtils.isEqualCollection(expected_vce_info_list, actual_vce_info_list));
	}

	/**
	 * Test the overridden {@link AbstractRVSISpecification#generateVersionConstraint(Timestamp)} method 
	 */
	@Test
	public void testGenerateVersionConstraintTimestamp()
	{
		this.sv_rvsi_spec.setVceInfoList(vce_info_xyzuv_list);
		AbstractVersionConstraint actual_sv_vc = this.sv_rvsi_spec.generateVersionConstraint(Timestamp.TIMESTAMP_INIT_ZERO);	// the parameter #sts for generating {@link SVSpecification} is not used.
		
		assertEquals("Fails to generate SV version constraint.", this.sv_vc, actual_sv_vc);
	}

	/**
	 * Test the inherited {@link AbstractRVSISpecification#generateVersionConstraint(QueryResults, Timestamp)} method
	 */
	@Test
	public void testSuperGenerateVersionConstraintQueryResultsTimestamp()
	{
		AbstractVersionConstraint actual_sv_vc = this.sv_rvsi_spec.generateVersionConstraint(this.query_results, Timestamp.TIMESTAMP_INIT_ZERO);	// the parameter #sts for generating {@link SVSpecification} is not used.

		assertEquals("Fails to generate SV version constraint.", this.sv_vc, actual_sv_vc);
	}
	
	@Test
	public void testJoin()
	{
		SortedSet<KVItem> expected_kv_set = this.kv_set;
		SortedSet<KVItem> actual_kv_set = this.sv_rvsi_spec.join(this.ck_set_wxyz, this.query_results);
		
		assertEquals("Join fails.", expected_kv_set, actual_kv_set);
	}

	@Test
	public void testExpand()
	{
		List<VCEntryRawInfo> expected_vce_info_list = this.vce_info_xyz_list;
		List<VCEntryRawInfo> actual_vce_info_list = this.sv_rvsi_spec.expand(this.kv_set, 2);
		
		assertEquals("Expand() ensures the order-preserving equality.", expected_vce_info_list, actual_vce_info_list);
	}

}
