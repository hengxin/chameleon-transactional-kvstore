package client.clientlibrary.rvsi.rvsispec;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

import client.clientlibrary.rvsi.vc.VCEntryRawInfo;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Cell;
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
	private long bound_t_s = 1;

	private final CompoundKey ck_rw_cw = new CompoundKey("Rw", "Cw");
	private final CompoundKey ck_rx_cx = new CompoundKey("Rx", "Cx");
	private final CompoundKey ck_ry_cy = new CompoundKey("Ry", "Cy");
	private final CompoundKey ck_rz_cz = new CompoundKey("Rz", "Cz");
	private final Set<CompoundKey> ck_set_wxyz = new HashSet<>();
	private long bound_w_x_y_z = 2;

	private final CompoundKey ck_ru_cu = new CompoundKey("Ru", "Cu");
	private final CompoundKey ck_rv_cv = new CompoundKey("Rv", "Cv");
	private long bound_u_v = 3;
	
	private ITimestampedCell ts_cell_t = new TimestampedCell(new Timestamp(1), new Cell("Cell_t"));

	private ITimestampedCell ts_cell_x = new TimestampedCell(new Timestamp(2), new Cell("Cell_x"));
	private ITimestampedCell ts_cell_y = new TimestampedCell(new Timestamp(3), new Cell("Cell_y"));
	private ITimestampedCell ts_cell_z = new TimestampedCell(new Timestamp(4), new Cell("Cell_z"));

	private ITimestampedCell ts_cell_u = new TimestampedCell(new Timestamp(5), new Cell("Cell_u"));
	private ITimestampedCell ts_cell_v = new TimestampedCell(new Timestamp(6), new Cell("Cell_v"));

	private final QueryResults query_results = new QueryResults();
	private final SortedSet<KVItem> kv_set = new TreeSet<>();
	private final List<VCEntryRawInfo> vce_info_xyz_list = new ArrayList<>();
	private final List<VCEntryRawInfo> vce_info_xyzuv_list = new ArrayList<>();
	
	@Before
	public void setUp() throws Exception
	{
		this.ck_set_wxyz.add(ck_rw_cw);
		this.ck_set_wxyz.add(ck_rx_cx);
		this.ck_set_wxyz.add(ck_ry_cy);
		this.ck_set_wxyz.add(ck_rz_cz);
		
		
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
		this.vce_info_xyzuv_list.add(vce_info_x_z_2);
		this.vce_info_xyzuv_list.add(vce_info_y_z_2);
		this.vce_info_xyzuv_list.add(vce_info_u_v_3);
	}

	@Test
	public void testExtractVCEntryRawInfo()
	{
		List<VCEntryRawInfo> expected_vce_info_list = this.vce_info_xyzuv_list;
		List<VCEntryRawInfo> actual_vce_info_list = this.sv_rvsi_spec.extractVCEntryRawInfo(query_results);
		
		System.out.println(expected_vce_info_list);
		System.out.println(actual_vce_info_list);
		
		assertTrue("Fails to extract three VCEntryRawInfo.", CollectionUtils.isEqualCollection(expected_vce_info_list, actual_vce_info_list));
	}

	@Test
	public void testGenerateVersionConstraintTimestamp()
	{
		fail("Not yet implemented"); // TODO
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
