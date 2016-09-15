/**
 * 
 */
package client.clientlibrary.rvsi.rvsispec;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import client.clientlibrary.rvsi.vc.VCEntryRawInfo;
import client.clientlibrary.transaction.QueryResults;
import kvs.component.Cell;
import kvs.component.Ordinal;
import kvs.component.Timestamp;
import kvs.compound.CompoundKey;
import kvs.compound.TimestampedCell;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertThat;

/**
 * @author hengxin
 * @date Created on 11-17-2015
 * 
 * <p> Test case for {@link AbstractRVSISpecificationTest}.
 */
public class AbstractRVSISpecificationTest {
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

	/**
	 * Test method for {@link client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification#addSpec(java.util.HashSet, int)}.
	 */
	@Test
	public void testAddSpec() {
	}

	/**
	 * Test method for {@link client.clientlibrary.rvsi.rvsispec.AbstractRVSISpecification#flattenRVSISpecMap()}.
	 */
	@Test
	public void testFlattenRVSISpecMap() {
		Map<CompoundKey, Integer> flatten_map = this.rvsiSpec.flattenRVSISpecMap();
		
		@SuppressWarnings("serial")
		Map<CompoundKey, Integer> expected_map = new HashMap<CompoundKey, Integer>() {
				{
					put(ck_r1_c1, 1);
					put(ck_r1_c2, 1);
					put(ck_r2_c1, 2);
					put(ck_r2_c2, 2);
				}
		};
		
		Assert.assertEquals("Test the flatten method.", expected_map, flatten_map);
	}

	@Test
	public void testExtractVCEntryRawInfo() {
		List<VCEntryRawInfo> actual_vce_info_list = this.rvsiSpec.extractVCEntryRawInfo(this.queryResults);
		
		List<VCEntryRawInfo> expected_vce_info_list = new ArrayList<>();
		expected_vce_info_list.add(new VCEntryRawInfo(ck_r1_c1, tc1, 1));
		expected_vce_info_list.add(new VCEntryRawInfo(ck_r2_c2, tc2, 2));
			
		assertThat("Test the extractVCEntryRawInfo() method", actual_vce_info_list, containsInAnyOrder(expected_vce_info_list.toArray()));
	}

}
