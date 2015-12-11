/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import client.clientlibrary.rvsi.vc.VCEntryRawInfo;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Cell;
import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.TimestampedCell;

/**
 * @author hengxin
 * @date Created on 11-17-2015
 * 
 * <p> Test case for {@link AbstractRVSISpecificationTest}.
 */
public class AbstractRVSISpecificationTest
{
	private AbstractRVSISpecification rvsi_spec = new BVSpecification();
	private QueryResults query_results = new QueryResults();

	private CompoundKey ck_r1_c1 = new CompoundKey("R1", "C1");
	private CompoundKey ck_r1_c2 = new CompoundKey("R1", "C2");
	private CompoundKey ck_r2_c1 = new CompoundKey("R2", "C1");
	private CompoundKey ck_r2_c2 = new CompoundKey("R2", "C2");

	private TimestampedCell tc1 = new TimestampedCell(Timestamp.TIMESTAMP_INIT, Ordinal.ORDINAL_INIT, new Cell("R1C1"));
	private TimestampedCell tc2 = new TimestampedCell(Timestamp.TIMESTAMP_INIT, Ordinal.ORDINAL_INIT, new Cell("R2C2"));
	
	@Before
	public void setUp() throws Exception
	{
		HashSet<CompoundKey> ckey_set_r1 = Stream.of(ck_r1_c1, ck_r1_c2)
				.collect(Collectors.toCollection(HashSet::new));
		
		HashSet<CompoundKey> ckey_set_r2 = Stream.of(ck_r2_c1, ck_r2_c2)
				.collect(Collectors.toCollection(HashSet::new));
		
		this.rvsi_spec.addSpec(ckey_set_r1, 1);
		this.rvsi_spec.addSpec(ckey_set_r2, 2);

		// set the the {@link #query_results}
		this.query_results.put(ck_r1_c1, tc1);
		this.query_results.put(ck_r2_c2, tc2);
	}

	/**
	 * Test method for {@link client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification#addSpec(java.util.HashSet, int)}.
	 */
	@Test
	public void testAddSpec()
	{
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification#flattenRVSISpecMap()}.
	 */
	@Test
	public void testFlattenRVSISpecMap()
	{
		Map<CompoundKey, Long> flatten_map = this.rvsi_spec.flattenRVSISpecMap();
		
		@SuppressWarnings("serial")
		Map<CompoundKey, Long> expected_map = new HashMap<CompoundKey, Long>()
		{
				{
					put(ck_r1_c1, 1L);
					put(ck_r1_c2, 1L);
					put(ck_r2_c1, 2L);
					put(ck_r2_c2, 2L);
				}
		};
		
		Assert.assertEquals("Test the flatten method.", expected_map, flatten_map);
	}

	@Test
	public void testExtractVCEntryRawInfo()
	{
		List<VCEntryRawInfo> actual_vce_info_list = this.rvsi_spec.extractVCEntryRawInfo(this.query_results);
		
		List<VCEntryRawInfo> expected_vce_info_list = new ArrayList<>();
		expected_vce_info_list.add(new VCEntryRawInfo(ck_r1_c1, tc1, 1));
		expected_vce_info_list.add(new VCEntryRawInfo(ck_r2_c2, tc2, 2));
			
		assertThat("Test the extractVCEntryRawInfo() method", actual_vce_info_list, containsInAnyOrder(expected_vce_info_list.toArray()));
	}

	/**
	 * Test method for {@link client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification#generateVersionConstraint(client.clientlibrary.transaction.QueryResults)}.
	 */
	@Test
	public void testGenerateVersionConstraint()
	{
		fail("Not yet implemented"); // TODO
	}

}
